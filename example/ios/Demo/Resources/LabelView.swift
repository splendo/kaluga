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

struct LabelView: View {

    let lifecycleViewModel: LifecycleViewModel<LabelViewModel>
    @ObservedObject var labels: ListObservable<KalugaLabel>

    init() {
        let viewModel = LabelViewModel(styledStringBuilderProvider: StyledStringBuilder.Provider())
        labels = ListObservable(viewModel.labels)
        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        lifecycleViewModel.lifecycleView { _ in
            ScrollView {
                VStack(spacing: 10.0) {
                    ForEach(labels.value, id: \.self) { label in
                        label.toText().frame(maxWidth: .infinity, alignment: .leading)
                    }
                }.padding(8)
            }.navigationTitle("feature_resources_button".localized())
        }
    }
}

struct LabelView_Previews: PreviewProvider {
    static var previews: some View {
        LabelView()
    }
}
