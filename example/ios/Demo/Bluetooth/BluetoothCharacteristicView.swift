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

struct BluetoothCharacteristicView: View {
    
    let characteristic: BluetoothCharacteristicViewModel
    
    @ObservedObject private var value: StringObservable
    @ObservedObject private var descriptors: ListObservable<BluetoothDescriptorViewModel>
    
    init(characteristic: BluetoothCharacteristicViewModel) {
        self.characteristic = characteristic
        
        value = StringObservable(characteristic.value)
        descriptors = ListObservable(characteristic.descriptors)
    }
    
    var body: some View {
        VStack {
            HStack {
                Text(characteristic.uuid).font(.system(size: 12.0)).opacity(0.8)
                Spacer()
                Text(value.value).font(.system(size: 12.0)).opacity(0.8)
            }
            HStack {
                Spacer().frame(width: 10)
                VStack {
                    Text("Descriptors").font(.system(size: 12.0))
                    ForEach(descriptors.value, id: \.self) { descriptor in
                        BluetoothDescriptorView(descriptor: descriptor)
                    }
                }
            }.background(Color("darker_color"))
        }.onAppear {
            characteristic.onResume()
        }.onDisappear {
            characteristic.onPause()
        }
    }
}
