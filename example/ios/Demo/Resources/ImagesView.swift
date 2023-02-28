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

struct ImagesView: View {

    let lifecycleViewModel: LifecycleViewModel<ImagesViewModel>

    init() {
        let viewModel = ImagesViewModel()
        lifecycleViewModel = LifecycleViewModel(viewModel)
    }

    var body: some View {
        lifecycleViewModel.lifecycleView { viewModel in
            ScrollView {
                HStack {
                    VStack(spacing: 10.0) {
                        ForEach(viewModel.images, id: \.self) { image in
                            Image(uiImage: image)
                        }
                    }
                    .frame(maxWidth: .infinity)
                    VStack(spacing: 10.0) {
                        ForEach(viewModel.tintedImages, id: \.self) { tintedImage in
                            tintedImage.swiftUI
                        }
                    }
                    .frame(maxWidth: .infinity)
                }
            }.navigationTitle("feature_resources_image".localized())
        }
    }
}

struct ImagesView_Previews: PreviewProvider {
    static var previews: some View {
        ImagesView()
    }
}
