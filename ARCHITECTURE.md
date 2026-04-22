# Architecture

Jung Tarot follows **MVVM (Model-View-ViewModel)** with a clean separation into three layers: `data`, `domain`, and `ui`.

---

## Layer Overview

```
┌─────────────────────────────────────┐
│              UI Layer               │
│  HomeScreen / ResultScreen          │
│  Observes StateFlow from ViewModel  │
└────────────────┬────────────────────┘
                 │ observes
┌────────────────▼────────────────────┐
│           ViewModel Layer           │
│  HomeViewModel                      │
│  Holds UI state + triggers domain   │
└────────────────┬────────────────────┘
                 │ calls
┌────────────────▼────────────────────┐
│           Domain Layer              │
│  ReadingEngine → GeminiService      │
│  Validates input, calls AI API      │
└────────────────┬────────────────────┘
                 │ reads
┌────────────────▼────────────────────┐
│            Data Layer               │
│  TarotRepository                    │
│  Static card catalog + metadata     │
└─────────────────────────────────────┘
```

---

## Data Layer

**`TarotRepository`** holds the complete 22-card Major Arcana catalog. Each card has:
- A unique `id` used as the canonical key throughout the app
- A French name (Tarot de Marseille original)
- A Spanish name map used exclusively inside AI-generated readings

**`TarotCard`** and **`CardMetadata`** are pure data classes with no dependencies.

**`SpreadType`** is an enum that owns its own display logic (`displayName()`, `description()`, `cardCount`), keeping spread-related logic co-located.

---

## Domain Layer

**`ReadingEngine`** is the single entry point for generating a reading. It:
1. Validates that the number of selected cards matches the spread
2. Resolves card IDs to full `CardMetadata` objects
3. Delegates to `GeminiService` for AI generation

**`GeminiService`** wraps the Gemini SDK. Key decisions:
- The system prompt encodes the full Jodorowsky + Jung interpretive framework
- The user prompt provides card positions and names in Spanish
- Retry logic with exponential backoff (2s, 4s, 8s) handles transient network failures
- All exceptions are caught and returned as user-facing Spanish strings — the service never throws

**`ReadingModels`** contains only plain data classes (`ReadingRequest`, `TarotReading`, `ReadingError`). No logic lives here.

---

## ViewModel Layer

**`HomeViewModel`** owns a single `TarotRepository` instance shared with `ReadingEngine` and `GeminiService` — no duplicate instantiation.

State is split into two `StateFlow`s intentionally:
- `state: StateFlow<HomeUiState>` — transient UI state (selected cards, loading, one-shot events)
- `currentReading: StateFlow<TarotReading?>` — persists the last reading independently of navigation events

`HomeEvent` is a sealed class used as a one-shot signal. `ReadingReady` carries no data — the reading lives in `currentReading`. This prevents the common bug where navigating to the result screen after the event is consumed results in a blank screen.

---

## UI Layer

**`HomeScreen`** is stateless — it receives all state and callbacks as parameters. Dialog state (`deckDialog`) is the only local `remember` state, which is appropriate since it's purely UI-local.

**`ResultScreen`** reads directly from `currentReading` StateFlow, not from the navigation event. This means the screen survives configuration changes and back-stack restoration correctly.

**Navigation** uses Navigation Compose with two routes (`home`, `result`). The `NavController` lives in `MainActivity` alongside the `ViewModel`, ensuring both share the same lifecycle scope.

---

## Key Design Decisions

**Why no Hilt/Koin?**
The dependency graph is shallow (one repository, one service, one engine). Manual injection via constructor defaults keeps the project readable without framework overhead. This is a deliberate tradeoff for a project of this scope.

**Why StateFlow over LiveData?**
StateFlow integrates naturally with Kotlin coroutines and Compose's `collectAsState()`. It also has a well-defined initial value, which eliminates null-safety boilerplate.

**Why is the AI prompt in the domain layer, not a resource file?**
The system prompt is tightly coupled to the domain logic (it references specific archetypes and interpretation rules). Keeping it in `GeminiService` makes it easier to version and test alongside the service that uses it.
