# Retry Mechanism with Exponential Backoff

## Overview

GeminiService now implements a robust retry mechanism with exponential backoff for handling transient network errors during Gemini API calls. This ensures reliability while respecting API rate limits and network conditions.

## Implementation Details

### Architecture

The retry mechanism is implemented as a private suspend inline function that wraps API calls:

```kotlin
private suspend inline fun <T> retryWithExponentialBackoff(
    maxRetries: Int = 3,
    initialDelayMs: Long = 2000,
    crossinline block: suspend () -> T
): T
```

### Key Features

#### 1. **Exponential Backoff Delays**
- Initial delay: **2 seconds**
- Each retry doubles the delay: `2^attemptNumber`
- Actual sequence: **2s → 4s → 8s**
- Reduces server load during transient outages

#### 2. **Retryable Error Types**
The following exceptions trigger automatic retries:

| Exception Type | Reason | Retry |
|---|---|---|
| `IOException` | Network connectivity issues | ✅ Yes |
| `SocketTimeoutException` | Socket-level timeout | ✅ Yes |
| `TimeoutException` | General timeout (Coroutines) | ✅ Yes |
| `IllegalArgumentException` | Invalid request parameters | ❌ No |
| Other `Exception` types | Unknown errors | ❌ No |

#### 3. **Spanish Error Messages**
All user-facing error messages remain in Spanish:

- "Conexión perdida con el oráculo. Por favor, verifica tu conexión de internet."
- "El oráculo tardó demasiado en responder. Por favor, intenta de nuevo."
- "Se agotó el tiempo esperando al oráculo. Por favor, intenta de nuevo."
- "Solicitud inválida al oráculo. Por favor, intenta de nuevo."

#### 4. **Comprehensive Logging**

Each retry attempt is logged at appropriate levels:

```
[DEBUG] Intento 1/3 de conexión con Gemini API
[WARN]  Intento 1/3 falló (reintentable): Connection timeout
[DEBUG] Esperando 2000ms antes del siguiente reintento...
[DEBUG] Solicitud a Gemini API exitosa en intento 2
```

Or on failure:

```
[DEBUG] Intento 1/3 de conexión con Gemini API
[ERROR] Error después de todos los reintentos
[WARN]  Intento 3/3 falló (no reintentable): Invalid API key
```

### Usage

In `generateReading()`:

```kotlin
suspend fun generateReading(spreadType: SpreadType, cards: List<CardMetadata>): String {
    // ... validation code ...
    
    return try {
        retryWithExponentialBackoff(maxRetries = 3) {
            model.generateContent(prompt).text
                ?: "El oráculo guarda silencio. Intenta de nuevo."
        }
    } catch (e: Exception) {
        // Error handling with Spanish messages
    }
}
```

## Retry Flow Diagram

```
User Request
    ↓
Attempt 1 (Immediate)
    ├─ Success? → Return Result
    └─ Retryable Error? 
        ├─ No → Throw Exception
        └─ Yes → Wait 2s
            ↓
Attempt 2 (After 2s)
    ├─ Success? → Return Result
    └─ Retryable Error?
        ├─ No → Throw Exception
        └─ Yes → Wait 4s
            ↓
Attempt 3 (After 4s)
    ├─ Success? → Return Result
    └─ Any Error → Throw Exception
        ↓
Error Handler (Spanish Message)
```

## Configuration

### Default Parameters

- `maxRetries`: 3 attempts
- `initialDelayMs`: 2000 (2 seconds)
- These can be customized when calling (currently hardcoded for consistency)

### Example: Custom Configuration

To modify retry behavior (if needed in future):

```kotlin
// Increase retries to 5 with 1 second initial delay
retryWithExponentialBackoff(maxRetries = 5, initialDelayMs = 1000) {
    model.generateContent(prompt).text
        ?: "El oráculo guarda silencio."
}
```

## Performance Considerations

### Maximum Wait Time

Total maximum wait time across all retries:
- Attempt 1: Immediate
- Wait before Attempt 2: 2s
- Wait before Attempt 3: 4s
- **Total: ~6-7 seconds maximum**

This is acceptable for a user interaction, keeping the UI responsive.

### When Retries Are NOT Applied

- **Invalid API Key**: Fails immediately (no retries)
- **Malformed Requests**: Fails immediately (no retries)
- **Rate Limiting (429)**: Not wrapped in IOException, may need special handling
- **Authentication Errors (401, 403)**: Not wrapped in IOException, fails immediately

### Future Enhancements

For production, consider:
1. Adding `HttpException` detection for HTTP 429 (rate limit)
2. Implementing circuit breaker pattern
3. Adding metrics/telemetry for retry success rates
4. Jitter to prevent thundering herd (e.g., `delay(baseDelay + random(0..1000))`)

## Testing the Retry Mechanism

### Test Scenarios

#### Scenario 1: Network Timeout (Auto-Recovers)
```kotlin
// Simulate: Network timeout on attempt 1, success on attempt 2
// Expected: 2 second delay, then successful response
```

#### Scenario 2: Persistent Network Error
```kotlin
// Simulate: Network error on all 3 attempts
// Expected: User sees "Conexión perdida con el oráculo..." after ~6 seconds
```

#### Scenario 3: Invalid API Key
```kotlin
// Simulate: Invalid API key
// Expected: Immediate failure with no retries
```

## Logging Output Examples

### Successful Retry After 1 Timeout

```
D: Intento 1/3 de conexión con Gemini API
W: Intento 1/3 falló (reintentable): Socket timeout
D: Esperando 2000ms antes del siguiente reintento...
D: Intento 2/3 de conexión con Gemini API
D: Solicitud a Gemini API exitosa en intento 2
```

### All Retries Exhausted

```
D: Intento 1/3 de conexión con Gemini API
W: Intento 1/3 falló (reintentable): Connection refused
D: Esperando 2000ms antes del siguiente reintento...

D: Intento 2/3 de conexión con Gemini API
W: Intento 2/3 falló (reintentable): Connection refused
D: Esperando 4000ms antes del siguiente reintento...

D: Intento 3/3 de conexión con Gemini API
W: Intento 3/3 falló (reintentable): Connection refused
E: Error después de todos los reintentos
```

User sees: *"Conexión perdida con el oráculo. Por favor, verifica tu conexión de internet."*

## Code References

Location: [app/src/main/java/com/jungtarot/app/domain/GeminiService.kt](../../app/src/main/java/com/jungtarot/app/domain/GeminiService.kt)

Key methods:
- `suspend fun generateReading()` - Main entry point (lines ~29-55)
- `private suspend inline fun <T> retryWithExponentialBackoff()` - Retry logic (lines ~57-104)
- `private fun Exception.isRetryable()` - Error classification (lines ~111-119)

## Related Files

- [SECRETS.md](SECRETS.md) - API key configuration
- [GeminiService.kt](../../app/src/main/java/com/jungtarot/app/domain/GeminiService.kt) - Implementation
- [ReadingEngine.kt](../../app/src/main/java/com/jungtarot/app/domain/ReadingEngine.kt) - Caller

## Support & Debugging

### Enable Verbose Logging

Use logcat filters:
```
adb logcat GeminiService:D
```

### Common Issues

**Issue**: "Error después de todos los reintentos"
- **Cause**: All 3 attempts failed
- **Debug**: Check logcat for specific error types
- **Solution**: Check network connectivity or API key validity

**Issue**: App hangs for 6 seconds before showing error
- **Cause**: Normal - all retries are being executed
- **Expected behavior**: This is by design to handle transient errors
- **Solution**: Only trust stable network connections, or implement UI-level timeout

**Issue**: Rate limitting (429) errors not retried
- **Cause**: HTTP 429 not wrapped in IOException
- **TODO**: Add specific handling for HTTP-level exceptions in future
