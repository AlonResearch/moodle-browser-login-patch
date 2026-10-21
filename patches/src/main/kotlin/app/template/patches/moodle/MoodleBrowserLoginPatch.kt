package app.template.patches.moodle

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.rawResourcePatch

private const val LOGIN_CHUNK = "assets/www/29870.ac7792e084efe277.js"
private const val ORIGINAL = "e.isBrowserSSO=p.CoreLoginHelper.isSSOLoginNeeded(e.siteCheck.code)"
private const val REPLACEMENT = "e.isBrowserSSO=true"

private val moodle521 = Compatibility(
    name = "Moodle",
    packageName = "com.moodle.moodlemobile",
    apkFileType = ApkFileType.APK,
    appIconColor = 0xFF9800,
    targets = listOf(AppTarget(version = "5.2.1", versionCodes = null)),
)

@Suppress("unused")
val browserLoginPatch = rawResourcePatch(
    name = "Use browser for Moodle login",
    description = "Open Moodle site authentication in the system browser on any Moodle host.",
    default = true,
) {
    compatibleWith(moodle521)

    execute {
        val chunk = get(LOGIN_CHUNK)
        val contents = chunk.readText(Charsets.UTF_8)
        val occurrences = Regex(Regex.escape(ORIGINAL)).findAll(contents).count()
        check(occurrences == 1) {
            "Expected one Moodle 5.2.1 login-mode check in $LOGIN_CHUNK; found $occurrences. APK was left unchanged."
        }
        chunk.writeText(contents.replace(ORIGINAL, REPLACEMENT), Charsets.UTF_8)
    }
}
