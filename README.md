# Moodle Browser Login patch

A third-party Morphe patch for the official Moodle Android app. It tells Moodle 5.2.1 to use the system browser for site authentication on any Moodle host. This can help when an institution requires an external SSO page that does not work in the app's embedded login flow.

## Compatibility and limits

- Official Moodle Android app, version **5.2.1** (`com.moodle.moodlemobile`). Other app versions are not included in this release target.
- The patch changes client-side login routing. It does not add an authentication method, handle credentials, or fix Moodle server configuration.
- Browser SSO still depends on the Moodle site's authentication plugin returning to Moodle's mobile launch URL correctly. The site's Mobile web services and mobile app authentication must be enabled and configured. A server error such as **“Plugin not enabled or configured”** must be fixed by the Moodle site administrator; this patch cannot enable server-side plugins.
- Tried with Jyväskylä University's Moodle. Login reached the university login page; the mobile launch endpoint showed a configuration error before the user continued through the website login. Results may differ by site.

Use only with Moodle sites you trust. This project is unofficial and is not affiliated with Moodle or Morphe.

## Install in Morphe

Add this source using **Sources → + → Remote** and enter `AlonResearch/moodle-browser-login-patch`, or open the [Morphe add-source link](https://morphe.software/add-source?github=AlonResearch/moodle-browser-login-patch). Then select the official Moodle 5.2.1 APK, choose **Use browser for Moodle login**, apply the patch, and install the result.

If the site does not return to the app after authentication, ask its administrator to check Mobile web services and the SSO return redirect. Do not share login links containing `passport` parameters; they are temporary authentication tokens.

<!-- PATCHES_START EXPANDED -->
<!-- PATCHES_END -->

## Development

Build the Morphe patch bundle with `./gradlew buildAndroid`. The `.mpp` file is produced under `patches/build/libs/`.

## License

GPL-3.0. See [LICENSE](LICENSE) and [NOTICE](NOTICE).
