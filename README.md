# Moodle Browser Login patch

An experimental patch for use with [Morphe Manager](https://github.com/MorpheApp/morphe-manager), a general-purpose Android app patcher that can apply compatible patches to different apps. This bundle adds **Log in through browser** to the official Moodle Android app's first-login and reconnect screens. The site's normal password form and identity-provider choices remain available. It works across Moodle site hostnames when the official Android app version and each site's authentication setup are compatible.

## Read before using

**This is a login-routing workaround, not a guarantee that every Moodle server can authenticate the app.** Only Moodle Android **5.2.1** (`com.moodle.moodlemobile`) is supported. Other app versions require a new patch target.

**Website login and app login are different.** Browser cookies stay in the browser. The server must issue an app token and redirect back to Moodle. Merely reaching your courses in the browser does not prove the app has logged in.

**Experimental status:** full browser-to-app login and logout/reconnect completion are still awaiting independent confirmation. See the exact checks and gaps in [VERIFICATION.md](VERIFICATION.md). Keep the remove-account workaround below available.

### "Plugin not enabled or configured"

If the browser shows this error, tap **Continue**, then choose **Login with JYU credentials** (or your institution's SSO option) on Moodle's login page and finish the sign-in. The browser may reuse an existing university SSO session, so you usually do not need to log out first. Wait for the browser to return to the Moodle app, then check that the app itself is signed in; seeing Moodle in the browser alone is not enough. This Continue → institution login sequence is what the JYU user reported working.

Moodle's launch script sets a short-lived app-handoff cookie before checking whether browser login is enabled for the site. That can explain why the error appears while a later institution login still completes the handoff. It is an app-launch marker, not your browser's Moodle session cookie being copied into the app. The error does not necessarily mean mobile web services are disabled. This server-dependent workaround is not guaranteed on every Moodle site. If the app does not open or reconnect, use the website or ask the site administrator to check mobile authentication mode, mobile web services, token permissions, and the SSO return redirect. The patch cannot change those server settings.

### Reconnecting after logout

The earlier JYU-only patch changed first login but missed **Reconnect**. This version adds the browser button to both screens. If an older installation still shows only a password field, patch the original APK again with this bundle and install the update using the same Morphe signing key.

As a last resort, remove the saved account using the pencil/edit control on Moodle's **Accounts** screen, then add the site again. This removes local app account data, not your university account. **Sync any offline work first:** removing the saved account can discard cached data or unsynced work. Entering an SSO password in Moodle's native password field will not fix a login-method mismatch.

## Install in Morphe

1. Download the `.mpp` bundle from the newest experimental entry on [Releases](https://github.com/AlonResearch/moodle-browser-login-patch/releases) to your phone.
2. In [Morphe Manager](https://github.com/MorpheApp/morphe-manager), use **Sources > + > Local** and choose that `.mpp`. **Patch selections > Import** accepts selection JSON, not bundles. Replace the local source with a newer bundle when updating; automatic repository-source setup will follow a stable release.
3. Select the original official Moodle **5.2.1** APK from storage, enable **Add browser login option**, patch it, and install the result. Installing the original first is unnecessary.
4. Enter your Moodle site and use **Log in through browser** if the usual login fails. Choose your institution's login provider on its real website.

Android cannot update an app signed by a different key. Replacing a Play Store installation may require removing it, which clears its local data. Retain Morphe's signing key for subsequent patched updates.

Disable or remove the earlier JYU-only patch source before patching Moodle with this source. Use the original APK, not an already patched APK.

## Direct install APK

The experimental release also includes a pre-patched APK for Moodle Android **5.2.1**. It is signed with this project's release key so Android can install it directly. Android treats it as a different signer from the Play Store APK and from APKs signed by your Morphe installation. If Android refuses the install because of a signature conflict, sync offline work, uninstall the existing Moodle app, then install the release APK. Uninstalling clears local app data. This release key is used for future direct-install APK updates; do not expect an APK signed by another key to update this copy.

The release APK and `.mpp` apply the same patch. The `.mpp` route lets [Morphe Manager](https://github.com/MorpheApp/morphe-manager) sign the result with your own Morphe key; the direct APK route is simpler to install but requires the project release signature. Neither option guarantees that every Moodle server can complete its browser-to-app token handoff.

Official downloads: [Moodle](https://download.moodle.org/mobile/). This patch requires 5.2.1 even if that page later offers a newer version. Original development APK SHA-256: `96fdab4bc72376f4a766a1fe47f70e2306391ce5246cda7eff5486f697f8e61d`. The release contains a patched APK and a patch bundle, but not the private signing key or credentials.

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
