# Contributing to Jung Tarot

Thank you for your interest in contributing to Jung Tarot! This document provides guidelines and instructions for contributing.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [Coding Standards](#coding-standards)
- [Submitting Changes](#submitting-changes)
- [Reporting Bugs](#reporting-bugs)
- [Suggesting Features](#suggesting-features)

## Code of Conduct

This project adheres to a code of conduct that all contributors are expected to follow:

- Be respectful and inclusive
- Welcome newcomers and help them get started
- Focus on constructive feedback
- Respect differing viewpoints and experiences
- Accept responsibility and apologize for mistakes

## Getting Started

### Prerequisites

- Android Studio (latest stable version)
- JDK 17 or later
- Git
- Basic knowledge of Kotlin and Jetpack Compose

### Setting Up Your Development Environment

1. **Fork the repository**
   ```bash
   # Click "Fork" on GitHub, then clone your fork
   git clone https://github.com/YOUR_USERNAME/jungtarot.git
   cd jungtarot
   ```

2. **Add upstream remote**
   ```bash
   git remote add upstream https://github.com/lioncarballo88/jungtarot.git
   ```

3. **Open in Android Studio**
   - File > Open
   - Select the project directory
   - Wait for Gradle sync

4. **Build the project**
   ```bash
   ./gradlew build
   ```

5. **Run the app**
   - Connect a device or start an emulator
   - Click Run in Android Studio

## Development Workflow

### Creating a Feature Branch

Always create a new branch for your work:

```bash
git checkout -b feature/your-feature-name
# or
git checkout -b bugfix/issue-description
```

Branch naming conventions:
- `feature/` - New features
- `bugfix/` - Bug fixes
- `refactor/` - Code refactoring
- `docs/` - Documentation changes
- `test/` - Test additions or changes

### Making Changes

1. **Write clean, readable code**
   - Follow Kotlin coding conventions
   - Use meaningful variable and function names
   - Add comments for complex logic

2. **Test your changes**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

3. **Run lint checks**
   ```bash
   ./gradlew lint
   ```

4. **Keep commits atomic**
   - One logical change per commit
   - Write clear commit messages

### Keeping Your Fork Updated

```bash
git fetch upstream
git checkout main
git merge upstream/main
git push origin main
```

## Coding Standards

### Kotlin Style Guide

Follow the [official Kotlin style guide](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// Good: Clear naming, proper formatting
fun calculateReading(
    cards: List<TarotCard>,
    spreadType: SpreadType
): TarotReading {
    return when (spreadType) {
        SpreadType.ONE_CARD -> createOneCardReading(cards)
        SpreadType.TWO_CARD -> createTwoCardReading(cards)
        SpreadType.THREE_CARD -> createThreeCardReading(cards)
    }
}

// Bad: Unclear naming, poor formatting
fun calc(c:List<TarotCard>,s:SpreadType):TarotReading{
    if(s==SpreadType.ONE_CARD)return createOneCardReading(c)
    else if(s==SpreadType.TWO_CARD)return createTwoCardReading(c)
    else return createThreeCardReading(c)
}
```

### Compose Guidelines

- Use `@Composable` functions appropriately
- Keep composables small and focused
- Hoist state when necessary
- Use `remember` and `rememberSaveable` correctly
- Follow Material 3 design principles

```kotlin
// Good: Stateless, focused composable
@Composable
fun CardDisplay(
    card: TarotCard,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = card.name, style = MaterialTheme.typography.headlineSmall)
        Text(text = card.description, style = MaterialTheme.typography.bodyMedium)
    }
}
```

### Documentation

- Add KDoc comments for public APIs
- Document complex logic
- Update README.md if adding features
- Keep documentation up to date

```kotlin
/**
 * Generates a tarot reading based on selected cards and spread type.
 *
 * @param cards List of selected tarot cards
 * @param spreadType Type of spread (one, two, or three card)
 * @param question User's question for the reading
 * @return Complete tarot reading with interpretation
 */
fun generateReading(
    cards: List<TarotCard>,
    spreadType: SpreadType,
    question: String
): TarotReading
```

### Testing

- Write unit tests for business logic
- Add UI tests for critical user flows
- Maintain test coverage above 70%
- Use descriptive test names

```kotlin
@Test
fun `generateReading returns valid reading for three card spread`() {
    val cards = listOf(card1, card2, card3)
    val reading = generateReading(cards, SpreadType.THREE_CARD, "Test question")
    
    assertNotNull(reading)
    assertEquals(3, reading.cards.size)
    assertTrue(reading.interpretation.isNotEmpty())
}
```

## Submitting Changes

### Commit Messages

Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

```
type(scope): subject

body

footer
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

Examples:
```
feat(reading): add five card spread option

Implement Celtic Cross spread with position meanings
and enhanced interpretation logic.

Closes #123
```

```
fix(ui): correct card display on small screens

Cards were overlapping on devices with screen width < 360dp.
Adjusted layout to use proper responsive sizing.

Fixes #456
```

### Pull Request Process

1. **Update your branch**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```

3. **Create Pull Request**
   - Go to GitHub
   - Click "New Pull Request"
   - Select your branch
   - Fill in the PR template

4. **PR Description Should Include:**
   - Clear description of changes
   - Screenshots (for UI changes)
   - Reference to related issues
   - Testing performed
   - Breaking changes (if any)

5. **Code Review**
   - Address review comments
   - Push additional commits as needed
   - Request re-review when ready

6. **Merging**
   - Maintainers will merge once approved
   - PR will be squashed and merged

### PR Template

```markdown
## Description
Brief description of what this PR does

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Related Issues
Closes #123

## Testing
- [ ] Unit tests pass
- [ ] UI tests pass
- [ ] Manual testing completed

## Screenshots (if applicable)
[Add screenshots here]

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-reviewed the code
- [ ] Commented complex code
- [ ] Updated documentation
- [ ] Added tests
- [ ] All tests pass
```

## Reporting Bugs

### Before Submitting a Bug Report

1. Check the [existing issues](https://github.com/lioncarballo88/jungtarot/issues)
2. Update to the latest version
3. Verify it's reproducible

### How to Submit a Bug Report

Create an issue with:

- **Clear title**: Describe the bug in one sentence
- **Description**: Detailed description of the bug
- **Steps to reproduce**: Numbered steps to reproduce
- **Expected behavior**: What should happen
- **Actual behavior**: What actually happens
- **Screenshots**: If applicable
- **Environment**:
  - Android version
  - Device model
  - App version
- **Logs**: Relevant logcat output

Example:
```markdown
## Bug: App crashes when selecting The Fool card

**Description**
The app crashes when attempting to select The Fool card in a three-card spread.

**Steps to Reproduce**
1. Open the app
2. Select "Three Card Spread"
3. Click on "The Fool" card
4. App crashes

**Expected Behavior**
The Fool card should be added to the selected cards list.

**Actual Behavior**
App crashes with NullPointerException.

**Environment**
- Android: 13
- Device: Pixel 6
- App version: 1.0.0

**Logs**
```
E/AndroidRuntime: FATAL EXCEPTION: main
    java.lang.NullPointerException at ...
```
```

## Suggesting Features

### Before Suggesting a Feature

1. Check existing feature requests
2. Ensure it aligns with project goals
3. Consider if it can be implemented as a plugin/extension

### How to Suggest a Feature

Create an issue with:

- **Clear title**: Feature name
- **Problem**: What problem does this solve?
- **Solution**: Proposed solution
- **Alternatives**: Alternative solutions considered
- **Additional context**: Screenshots, mockups, examples

Example:
```markdown
## Feature Request: Daily Card Notification

**Problem**
Users want to receive a daily tarot card for reflection without opening the app.

**Proposed Solution**
Add an optional daily notification that:
- Sends at a user-configured time
- Shows a random card
- Includes a brief interpretation
- Opens full reading when tapped

**Alternatives Considered**
1. Widget instead of notification
2. In-app daily card screen only

**Additional Context**
Many tarot apps offer this feature. Example: [screenshot]
```

## Questions?

- Open a [discussion](https://github.com/lioncarballo88/jungtarot/discussions)
- Join our community chat (if available)
- Email the maintainers

## Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes
- Project credits

Thank you for contributing! 🎴✨
