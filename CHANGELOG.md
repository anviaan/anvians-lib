### Added
- Added `Config.initialize(String)` for platform-resolved configuration directories.
- Added documented `RegistryUtil` helpers for registry values, resource keys, and tag keys.

### Fixed
- Configurations now create their directories, use UTF-8, and recover from malformed or null JSON.
- Updated 26.1 resource metadata and removed references to missing mod icons.
- Telemetry now distinguishes successful HTTP responses from non-2xx responses.