# Moodle Browser Login patch

An experimental patch for use with Morphe. It adds **Log in through browser** to the official Moodle Android app's first-login and reconnect screens. The site's normal password form and identity-provider choices remain available. The button uses Moodle's existing browser SSO and token callback for any site hostname, subject to the site's authentication configuration.

## Read before using

**This is a login-routing workaround, not a guarantee that every Moodle server can authenticate the app.** Only Moodle Android **5.2.1** (`com.moodle.moodlemobile`) is supported. Other app versions require a new patch target.

**Website login and app login are different.** Browser cookies stay in the browser. The server must issue an app token and redirect back to Moodle. Merely reaching your courses in the browser does not prove the app has logged in.

### "Plugin not enabled or configured"

Moodle can show this error when its mobile authentication mode is not set to browser SSO. In Moodle's launch script, the handoff cookie is set before that configuration check. On JYU, the user reported that **Continue -> website login** then worked. That sequence depends on the server and browser session and is not guaranteed elsewhere. It does not necessarily mean the mobile web service itself is disabled.

If it appears, try **Continue**, choose your institution's login method, and complete authentication. Then check whether the Moodle app itself reconnects. If it does not, use the website directly or ask the site administrator to check mobile authentication mode, mobile web services, token permissions, and the SSO return redirect. This patch cannot change those server settings.

### Reconnecting after logout

The earlier JYU-only patch changed first login but missed **Reconnect**. This version adds the browser button to both screens. If an older installation still shows only a password field, patch the original APK again with this bundle and install the update using the same Morphe signing key.

As a last resort, remove the saved account using the pencil/edit control on Moodle's **Accounts** screen, then add the site again. This removes local app account data, not your university account. **Sync any offline work first:** removing the saved account can discard cached data or unsynced work. Entering an SSO password in Moodle's native password field will not fix a login-method mismatch.

## Install in Morphe

1. Add [this GitHub source](https://github.com/AlonResearch/moodle-browser-login-patch) under **Sources > + > Remote**, or open the [add-source link](https://morphe.software/add-source?github=AlonResearch/moodle-browser-login-patch).
2. Enable **Pre-release patches** for this source during the experimental release. Alternatively download the `.mpp` from [Releases](https://github.com/AlonResearch/moodle-browser-login-patch/releases) and use **Sources > + > Local**. **Patch selections > Import** accepts selection JSON, not bundles.
3. Select the original official Moodle **5.2.1** APK from storage, enable **Add browser login option**, patch it, and install the result. Installing the original first is unnecessary.
4. Enter your Moodle site and use **Log in through browser** if the usual login fails. Choose your institution's login provider on its real website.

Android cannot update an app signed by a different key. Replacing a Play Store installation may require removing it, which clears its local data. Retain Morphe's signing key for subsequent patched updates.

Disable or remove the earlier JYU-only patch source before patching Moodle with this source. Use the original APK, not an already patched APK.

Official downloads: [Moodle](https://download.moodle.org/mobile/). This patch requires 5.2.1 even if that page later offers a newer version. Original development APK SHA-256: `96fdab4bc72376f4a766a1fe47f70e2306391ce5246cda7eff5486f697f8e61d`. Releases contain patch bundles, not Moodle APKs or signing keys.

## How it works

The patch exposes Moodle's existing Angular browser button independently of the password form in two compiled JavaScript resources. It calls the existing SSO handler with system-browser mode (`2`), keeping Moodle's service, launch URL, reconnect data, and passport callback handling. Every resource anchor is validated before either resource is written; unsupported or already-patched APKs fail rather than receiving a partial patch.

It does not read passwords, copy cookies, install a proxy, collect analytics, or modify course modules. The browser button is for authentication and cannot add unsupported course features. Tokens remain subject to Moodle's existing server and app security checks. Avoid publishing complete login/callback URLs containing passport or token values.

See Moodle's [launch implementation](https://github.com/moodle/moodle/blob/MOODLE_500_STABLE/admin/tool/mobile/launch.php) and [SSO troubleshooting guide](https://docs.moodle.org/502/en/Moodle_app_FAQ#SSO_is_not_working_with_my_custom_auth_plugin). This project is independent of Moodle, Morphe, and JYU.

<!-- PATCHES_START EXPANDED -->
<!-- PATCHES_END -->

## Build

Use Java 21 and an Android SDK. CI builds pinned public sources for the Morphe plugin and patcher rather than relying on package-registry availability. For a local build, check out these dependencies alongside this repository:

| Dependency | Commit |
| --- | --- |
| [morphe-patches-gradle-plugin](https://github.com/MorpheApp/morphe-patches-gradle-plugin) | `0a2c1971e2913a6ea5e8fd192d359765169a6a06` |
| [morphe-patcher](https://github.com/MorpheApp/morphe-patcher) | `812e96eaac173cd058ba49e242325371959befae` |

Set `ORG_GRADLE_PROJECT_morphePluginSource` and `ORG_GRADLE_PROJECT_morphePatcherSource` to their absolute checkout paths, then run `./gradlew :patches:buildAndroid` (`gradlew.bat` on Windows). The upstream plugin also requires `GITHUB_ACTOR` and `GITHUB_TOKEN` environment variables. Keep credentials out of tracked files. Output: `patches/build/libs/patches-*.mpp`.

Pushes to `dev` publish experimental releases. Stable releases require an explicit workflow run on `main`.

## License

Based on the Morphe patches template. GPL-3.0, with [LICENSE](LICENSE) and [NOTICE](NOTICE).
