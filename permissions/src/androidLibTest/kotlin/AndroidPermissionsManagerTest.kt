import android.Manifest
import android.content.Context
import com.splendo.kaluga.permissions.AndroidPermissionsManager
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

@RunWith(MockitoJUnitRunner::class)
class AndroidPermissionsManagerTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var permissionsManager: PermissionManager<Permission.Storage>
    lateinit var androidPermissionsManager: AndroidPermissionsManager<Permission.Storage>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        androidPermissionsManager = AndroidPermissionsManager(context, permissionsManager, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
    }



}