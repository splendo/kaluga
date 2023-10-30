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

struct BluetoothServiceView: View {
    
    let service: BluetoothServiceViewModel
    
    @ObservedObject private var characteristics: ListObservable<BluetoothCharacteristicViewModel>
    
    init(service: BluetoothServiceViewModel) {
        self.service = service
        
        characteristics = ListObservable(service.characteristics)
    }
    
    var body: some View {
        VStack {
            HStack(spacing: 4) {
                Text("Service").font(.system(size: 12.0))
                Text(service.uuid).font(.system(size: 12.0)).opacity(0.8)
            }
            Text("Characteristics").font(.system(size: 12.0))
            ForEach(characteristics.value, id: \.self) { characteristic in
                BluetoothCharacteristicView(characteristic: characteristic)
            }
        }
    }
}
