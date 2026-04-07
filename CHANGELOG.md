### Changed
- Converted TelemetrySender HttpClient to thread-safe static singleton
- Added HTTP request timeout (10 seconds) for telemetry requests

### Added
- Added null parameter validation on public APIs
- Added Javadoc documentation to CommonMod and Services classes
- Added REQUEST_TIMEOUT constant for HTTP requests

### Removed
- Removed deprecated sendTelemetryDataLegacy method
- Removed unused eventBus parameter from NeoForgeMod constructor
- Extracted and removed duplicated directory creation logic

### Improved
- Refactored duplicate directory creation into createDirectoryIfNotExists helper method
- Enhanced code quality and maintainability
- Improved API robustness with input validation
- Added comprehensive inline documentation
- Port to minecraft 26.1