//
//  Copyright 2023 Splendo Consulting B.V. The Netherlands
// 
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//

import SwiftUI
import KalugaExampleShared

struct BluetoothDescriptorView: View, Equatable {
    
    static func == (lhs: BluetoothDescriptorView, rhs: BluetoothDescriptorView) -> Bool {
        lhs.descriptor.uuid == rhs.descriptor.uuid
    }    
    
    let descriptor: BluetoothDescriptorViewModel
    
    @ObservedObject private var value: StringObservable
    
    init(descriptor: BluetoothDescriptorViewModel) {
        self.descriptor = descriptor
        
        value = StringObservable(descriptor.value)
    }
    
    var body: some View {
        HStack {
            Text(descriptor.uuid).font(.system(size: 12.0)).opacity(0.8)
            Spacer()
            Text(value.value).font(.system(size: 12.0)).opacity(0.8)
        }
        .onAppear { descriptor.onResume() }
        .onDisappear { descriptor.onPause() }
    }
}
