# Maco Spotify API

A modern, type-safe Java library for interacting with the Spotify Web API. This library provides a clean and intuitive interface for accessing Spotify's features, including user authentication, top tracks, top artists, and user profile information.

## Features

- 🔐 OAuth 2.0 Authentication with Authorization Code Flow
- 🔄 Automatic token refresh and management
- 🎵 Access to user's top tracks and artists
- 👤 User profile information
- ⚡ Modern Java 21 features
- 🛡️ Type-safe API responses
- 📝 Comprehensive logging
- 🔍 Detailed error handling

## Requirements

- Java 21 or higher
- Maven 3.6 or higher

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.macocoding.spotify-api</groupId>
    <artifactId>spotify-api</artifactId>
    <version>4.0.1</version>
</dependency>
```

## Quick Start

```java
// Initialize the client
String clientId = "your-client-id";
String clientSecret = "your-client-secret";
String redirectUri = "your-redirect-uri";
String[] scopes = {"user-top-read", "user-read-private"};

SpotifyClient client = new SpotifyClient(clientId, clientSecret, redirectUri, scopes);

// Get authorization URL
String state = "random-state-string";
String authUrl = client.getAuthorizationUrl(state);

// After user authorization, authenticate with the code
client.authenticate(authCode);

// Get user's top tracks from the last 4 weeks
List<SpotifyTrack> topTracks = client.getTopTracksLast4Weeks(10, 0);

// Get user's top artists from the last 6 months
List<SpotifyArtist> topArtists = client.getTopArtistsLast6Months(10, 0);

// Get current user's profile
SpotifyUser user = client.getCurrentUserDetails();
```

## Authentication

The library supports the OAuth 2.0 Authorization Code Flow:

1. Get the authorization URL using `getAuthorizationUrl()`
2. Redirect the user to the URL
3. Handle the callback with the authorization code
4. Authenticate using `authenticate(code)`

### Token Management

The library automatically handles token refresh when needed. You can also manually refresh the token using:

```java
SpotifyToken newToken = client.refreshAccessToken();
```

To deauthenticate:

```java
client.deAuthenticate();
```

## Available Endpoints

### User Profile

- `getCurrentUserDetails()` - Get the current user's profile information

### Top Tracks

- `getTopTracksLast4Weeks(limit, offset)` - Get user's top tracks from the last 4 weeks
- `getTopTracksLast6Months(limit, offset)` - Get user's top tracks from the last 6 months
- `getTopTracksAllTime(limit, offset)` - Get user's all-time top tracks

### Top Artists

- `getTopArtistsLast4Weeks(limit, offset)` - Get user's top artists from the last 4 weeks
- `getTopArtistsLast6Months(limit, offset)` - Get user's top artists from the last 6 months
- `getTopArtistsAllTime(limit, offset)` - Get user's all-time top artists

## Error Handling

The library provides comprehensive error handling with detailed error messages. All API calls are wrapped in try-catch blocks and throw appropriate exceptions with meaningful messages.

## Logging

The library uses SLF4J for logging. Configure your preferred logging implementation to see detailed logs about API calls, token management, and error handling.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.txt) file for details.

## Author

- **Razvan Macovei** - [GitHub](https://github.com/razvanmacovei15)

## Support

If you encounter any issues or have questions, please open an issue on GitHub.
