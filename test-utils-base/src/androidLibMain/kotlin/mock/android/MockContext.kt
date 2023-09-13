/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.test.base.mock.android

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.UserHandle
import android.view.Display
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

@Suppress("RedundantNullableReturnType")
@SuppressLint("MissingPermission")
class MockContext : Context() {
    override fun getAssets(): AssetManager = error("not implemented")

    override fun getResources(): Resources = error("not implemented")

    override fun getPackageManager(): PackageManager = error("not implemented")

    override fun getContentResolver(): ContentResolver = error("not implemented")

    override fun getMainLooper(): Looper = error("not implemented")

    override fun getApplicationContext(): Context = error("not implemented")

    override fun setTheme(resid: Int) = error("not implemented")

    override fun getTheme(): Resources.Theme = error("not implemented")

    override fun getClassLoader(): ClassLoader = error("not implemented")

    override fun getPackageName(): String = error("not implemented")

    override fun getApplicationInfo(): ApplicationInfo = error("not implemented")

    override fun getPackageResourcePath(): String = error("not implemented")

    override fun getPackageCodePath(): String = error("not implemented")

    override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences = error("not implemented")

    override fun moveSharedPreferencesFrom(sourceContext: Context?, name: String?): Boolean = error("not implemented")

    override fun deleteSharedPreferences(name: String?): Boolean = error("not implemented")

    override fun openFileInput(name: String?): FileInputStream = error("not implemented")

    override fun openFileOutput(name: String?, mode: Int): FileOutputStream = error("not implemented")

    override fun deleteFile(name: String?): Boolean = error("not implemented")

    override fun getFileStreamPath(name: String?): File = error("not implemented")

    override fun getDataDir(): File = error("not implemented")

    override fun getFilesDir(): File = error("not implemented")

    override fun getNoBackupFilesDir(): File = error("not implemented")

    override fun getExternalFilesDir(type: String?): File? = error("not implemented")

    override fun getExternalFilesDirs(type: String?): Array<File> = error("not implemented")

    override fun getObbDir(): File = error("not implemented")

    override fun getObbDirs(): Array<File> = error("not implemented")

    override fun getCacheDir(): File = error("not implemented")

    override fun getCodeCacheDir(): File = error("not implemented")

    override fun getExternalCacheDir(): File? = error("not implemented")

    override fun getExternalCacheDirs(): Array<File> = error("not implemented")

    override fun getExternalMediaDirs(): Array<File> = error("not implemented")

    override fun fileList(): Array<String> = error("not implemented")

    override fun getDir(name: String?, mode: Int): File = error("not implemented")

    override fun openOrCreateDatabase(
        name: String?,
        mode: Int,
        factory: SQLiteDatabase.CursorFactory?,
    ): SQLiteDatabase = error("not implemented")

    override fun openOrCreateDatabase(
        name: String?,
        mode: Int,
        factory: SQLiteDatabase.CursorFactory?,
        errorHandler: DatabaseErrorHandler?,
    ): SQLiteDatabase = error("not implemented")

    override fun moveDatabaseFrom(sourceContext: Context?, name: String?): Boolean = error("not implemented")

    override fun deleteDatabase(name: String?): Boolean = error("not implemented")

    override fun getDatabasePath(name: String?): File = error("not implemented")

    override fun databaseList(): Array<String> = error("not implemented")

    override fun getWallpaper(): Drawable = error("not implemented")

    override fun peekWallpaper(): Drawable = error("not implemented")

    override fun getWallpaperDesiredMinimumWidth(): Int = error("not implemented")

    override fun getWallpaperDesiredMinimumHeight(): Int = error("not implemented")

    override fun setWallpaper(bitmap: Bitmap?) = error("not implemented")

    override fun setWallpaper(data: InputStream?) = error("not implemented")

    override fun clearWallpaper() = error("not implemented")

    override fun startActivity(intent: Intent?) = error("not implemented")

    override fun startActivity(intent: Intent?, options: Bundle?) = error("not implemented")

    override fun startActivities(intents: Array<out Intent>?) = error("not implemented")

    override fun startActivities(intents: Array<out Intent>?, options: Bundle?) = error("not implemented")

    override fun startIntentSender(
        intent: IntentSender?,
        fillInIntent: Intent?,
        flagsMask: Int,
        flagsValues: Int,
        extraFlags: Int,
    ) = error("not implemented")

    override fun startIntentSender(
        intent: IntentSender?,
        fillInIntent: Intent?,
        flagsMask: Int,
        flagsValues: Int,
        extraFlags: Int,
        options: Bundle?,
    ) = error("not implemented")

    override fun sendBroadcast(intent: Intent?) = error("not implemented")

    override fun sendBroadcast(intent: Intent?, receiverPermission: String?) = error("not implemented")

    override fun sendOrderedBroadcast(intent: Intent?, receiverPermission: String?) = error("not implemented")

    override fun sendOrderedBroadcast(
        intent: Intent,
        receiverPermission: String?,
        resultReceiver: BroadcastReceiver?,
        scheduler: Handler?,
        initialCode: Int,
        initialData: String?,
        initialExtras: Bundle?,
    ) = error("not implemented")

    override fun sendBroadcastAsUser(intent: Intent?, user: UserHandle?) = error("not implemented")

    override fun sendBroadcastAsUser(
        intent: Intent?,
        user: UserHandle?,
        receiverPermission: String?,
    ) = error("not implemented")

    override fun sendOrderedBroadcastAsUser(
        intent: Intent?,
        user: UserHandle?,
        receiverPermission: String?,
        resultReceiver: BroadcastReceiver?,
        scheduler: Handler?,
        initialCode: Int,
        initialData: String?,
        initialExtras: Bundle?,
    ) = error("not implemented")

    override fun sendStickyBroadcast(intent: Intent?) = error("not implemented")

    override fun sendStickyOrderedBroadcast(
        intent: Intent?,
        resultReceiver: BroadcastReceiver?,
        scheduler: Handler?,
        initialCode: Int,
        initialData: String?,
        initialExtras: Bundle?,
    ) = error("not implemented")

    override fun removeStickyBroadcast(intent: Intent?) = error("not implemented")

    override fun sendStickyBroadcastAsUser(intent: Intent?, user: UserHandle?) = error("not implemented")

    override fun sendStickyOrderedBroadcastAsUser(
        intent: Intent?,
        user: UserHandle?,
        resultReceiver: BroadcastReceiver?,
        scheduler: Handler?,
        initialCode: Int,
        initialData: String?,
        initialExtras: Bundle?,
    ) = error("not implemented")

    override fun removeStickyBroadcastAsUser(intent: Intent?, user: UserHandle?) = error("not implemented")

    override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter?): Intent? = error("not implemented")

    override fun registerReceiver(
        receiver: BroadcastReceiver?,
        filter: IntentFilter?,
        flags: Int,
    ): Intent? = error("not implemented")

    override fun registerReceiver(
        receiver: BroadcastReceiver?,
        filter: IntentFilter?,
        broadcastPermission: String?,
        scheduler: Handler?,
    ): Intent? = error("not implemented")

    override fun registerReceiver(
        receiver: BroadcastReceiver?,
        filter: IntentFilter?,
        broadcastPermission: String?,
        scheduler: Handler?,
        flags: Int,
    ): Intent? = error("not implemented")

    override fun unregisterReceiver(receiver: BroadcastReceiver?) = error("not implemented")

    override fun startService(service: Intent?): ComponentName? = error("not implemented")

    override fun startForegroundService(service: Intent?): ComponentName? = error("not implemented")

    override fun stopService(service: Intent?): Boolean = error("not implemented")

    override fun bindService(service: Intent, conn: ServiceConnection, flags: Int): Boolean = error("not implemented")

    override fun unbindService(conn: ServiceConnection) = error("not implemented")

    override fun startInstrumentation(
        className: ComponentName,
        profileFile: String?,
        arguments: Bundle?,
    ): Boolean = error("not implemented")

    override fun getSystemService(name: String): Any = error("not implemented")

    override fun getSystemServiceName(serviceClass: Class<*>): String? = error("not implemented")

    override fun checkPermission(permission: String, pid: Int, uid: Int): Int = error("not implemented")

    override fun checkCallingPermission(permission: String): Int = error("not implemented")

    override fun checkCallingOrSelfPermission(permission: String): Int = error("not implemented")

    override fun checkSelfPermission(permission: String): Int = error("not implemented")

    override fun enforcePermission(permission: String, pid: Int, uid: Int, message: String?) = error("not implemented")

    override fun enforceCallingPermission(permission: String, message: String?) = error("not implemented")

    override fun enforceCallingOrSelfPermission(permission: String, message: String?) = error("not implemented")

    override fun grantUriPermission(toPackage: String?, uri: Uri?, modeFlags: Int) = error("not implemented")

    override fun revokeUriPermission(uri: Uri?, modeFlags: Int) = error("not implemented")

    override fun revokeUriPermission(toPackage: String?, uri: Uri?, modeFlags: Int) = error("not implemented")

    override fun checkUriPermission(uri: Uri?, pid: Int, uid: Int, modeFlags: Int): Int = error("not implemented")

    override fun checkUriPermission(
        uri: Uri?,
        readPermission: String?,
        writePermission: String?,
        pid: Int,
        uid: Int,
        modeFlags: Int,
    ): Int = error("not implemented")

    override fun checkCallingUriPermission(uri: Uri?, modeFlags: Int): Int = error("not implemented")

    override fun checkCallingOrSelfUriPermission(uri: Uri?, modeFlags: Int): Int = error("not implemented")

    override fun enforceUriPermission(
        uri: Uri?,
        pid: Int,
        uid: Int,
        modeFlags: Int,
        message: String?,
    ) = error("not implemented")

    override fun enforceUriPermission(
        uri: Uri?,
        readPermission: String?,
        writePermission: String?,
        pid: Int,
        uid: Int,
        modeFlags: Int,
        message: String?,
    ) = error("not implemented")

    override fun enforceCallingUriPermission(uri: Uri?, modeFlags: Int, message: String?) = error("not implemented")

    override fun enforceCallingOrSelfUriPermission(uri: Uri?, modeFlags: Int, message: String?) = error("not implemented")

    override fun createPackageContext(packageName: String?, flags: Int): Context = error("not implemented")

    override fun createContextForSplit(splitName: String?): Context = error("not implemented")

    override fun createConfigurationContext(overrideConfiguration: Configuration): Context = error("not implemented")

    override fun createDisplayContext(display: Display): Context = error("not implemented")

    override fun createDeviceProtectedStorageContext(): Context = error("not implemented")

    override fun isDeviceProtectedStorage(): Boolean = error("not implemented")
}
