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

struct ColorView: View, Equatable {
    
    static func == (lhs: ColorView, rhs: ColorView) -> Bool {
        return true
    }

    let lifecycleViewModel: LifecycleViewModel<ColorViewModel>
    private let disposeBag = DisposeBag()
    @ObservedObject var backdropColorBackground: BackgroundStyleObservable
    @ObservedObject var blendedColorBackground: BackgroundStyleObservable
    @ObservedObject var sourceColorBackground: BackgroundStyleObservable

    @State var backdropText: String = ""
    @State var sourceText: String = ""

    @ObservedObject var blendModeButton: ButtonObservable
    @ObservedObject var lightenedBackdrops: ListObservable<KalugaBackgroundStyle>
    @ObservedObject var darkenedBackdrops: ListObservable<KalugaBackgroundStyle>
    @ObservedObject var lightenedBlended: ListObservable<KalugaBackgroundStyle>
    @ObservedObject var darkenedBlended: ListObservable<KalugaBackgroundStyle>
    @ObservedObject var lightenedSource: ListObservable<KalugaBackgroundStyle>
    @ObservedObject var darkenedSource: ListObservable<KalugaBackgroundStyle>

    init() {
        let containerView = ContainerView(.alert)
        let viewModel = ColorViewModel(alertPresenterBuilder: containerView.alertBuilder)
        backdropColorBackground = BackgroundStyleObservable(viewModel.backdropColorBackground)
        blendedColorBackground = BackgroundStyleObservable(viewModel.blendedColorBackground)
        sourceColorBackground = BackgroundStyleObservable(viewModel.sourceColorBackground)
        lifecycleViewModel = LifecycleViewModel(viewModel, containerView: containerView)

        blendModeButton = ButtonObservable(viewModel.blendModeButton)

        lightenedBackdrops = ListObservable(viewModel.lightenBackdrops)
        darkenedBackdrops = ListObservable(viewModel.darkenBackdrops)
        lightenedBlended = ListObservable(viewModel.lightenBlended)
        darkenedBlended = ListObservable(viewModel.darkenBlended)
        lightenedSource = ListObservable(viewModel.lightenSource)
        darkenedSource = ListObservable(viewModel.darkenSource)
    }

    var body: some View {
        lifecycleViewModel.lifecycleView { viewModel in
            ScrollView {
                HStack(alignment: .top, spacing: 10.0) {
                    VStack(alignment: .center, spacing: 5.0) {
                        Spacer()
                            .frame(width: 100, height: 100, alignment: .center)
                            .background(backdropColorBackground.value)
                        TextField("", text: $backdropText)
                            .onSubmit {
                                viewModel.submitBackdropText(backdropText: backdropText)
                            }
                            .textFieldStyle(.roundedBorder)
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                    Spacer()
                        .frame(width: 100, height: 100, alignment: .center)
                        .background(blendedColorBackground.value)
                    VStack(alignment: .center, spacing: 5.0) {
                        Spacer()
                            .frame(width: 100, height: 100, alignment: .center)
                            .background(sourceColorBackground.value)
                        TextField("", text: $sourceText).onSubmit {
                            viewModel.submitSourceText(sourceText: sourceText)
                        }
                        .textFieldStyle(.roundedBorder)
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                }
                blendModeButton.value.toButton()
                viewModel.flipButton.toButton()
                lightenedBackdrops.value.asHorizontalScrollView()
                darkenedBackdrops.value.asHorizontalScrollView()
                lightenedBlended.value.asHorizontalScrollView()
                darkenedBlended.value.asHorizontalScrollView()
                lightenedSource.value.asHorizontalScrollView()
                darkenedSource.value.asHorizontalScrollView()
                
                Text("resources_color_current_mode".localized())
                viewModel.currentModeColors.asHorizontalScrollView()
                
                Text("resources_color_light_mode".localized())
                viewModel.lightModeColors.asHorizontalScrollView()
                
                Text("resources_color_dark_mode".localized())
                viewModel.darkModeColors.asHorizontalScrollView()
            }
            .navigationTitle("feature_resources_color".localized())
            .onAppear {
                disposeBag.add(
                    disposable: viewModel.backdropText.observe { next in
                        if let backdrop = next as? String {
                            backdropText = backdrop
                        }
                    }
                )
                disposeBag.add(
                    disposable: viewModel.sourceText.observe { next in
                        if let source = next as? String {
                            sourceText = source
                        }
                    }
                )
            }
            .onDisappear {
                disposeBag.dispose()
            }
        }
    }
}

extension Array<KalugaBackgroundStyle> {
    func asHorizontalScrollView() -> some View {
        ScrollView(.horizontal) {
            HStack(spacing: 2.0) {
                ForEach(self, id: \.self) { backgroundStyle in
                    Spacer()
                        .frame(width: 50.0, height: 50.0)
                        .background(backgroundStyle)
                }
            }.padding(.horizontal, 10.0)
        }
    }
}

struct ColorView_Previews: PreviewProvider {
    static var previews: some View {
        ColorView()
    }
}
