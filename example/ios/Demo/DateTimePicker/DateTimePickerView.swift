//
//  Copyright 2022 Splendo Consulting B.V. The Netherlands
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

struct DateTimePickerView: View {

    @ObservedObject var dateLabel: StringObservable
    let lifecycleViewModel: LifecycleViewModel<DateTimePickerViewModel>

    init() {
        let container = ContainerView(.datePicker)
        let viewModel = DateTimePickerViewModel(dateTimePickerPresenterBuilder: container.datePickerBuilder)
        dateLabel = StringObservable(viewModel.dateLabel)
        lifecycleViewModel = LifecycleViewModel(viewModel, containerView: container)
    }

    var body: some View {
        lifecycleViewModel.lifecycleView { viewModel in
            VStack {
                HStack(spacing: 10.0) {
                    viewModel.selectDateButton.toButton(
                        buttonFrame: .frame(maxWidth: .infinity)
                    )
                    viewModel.selectTimeButton.toButton(
                        buttonFrame: .frame(maxWidth: .infinity)
                    )
                }
                Text(viewModel.currentTimeTitle)
                Text(dateLabel.value)
                Spacer()
            }
        }.padding(10.0)
            .navigationTitle(Text("feature_date_time_picker".localized()))
    }
}

struct DateTimePickerView_Previews: PreviewProvider {
    static var previews: some View {
        DateTimePickerView()
    }
}
