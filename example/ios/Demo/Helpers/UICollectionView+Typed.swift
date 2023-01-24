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

import UIKit

extension UICollectionView {

    func dequeueTypedReusableCell<TypedCell: UICollectionViewCell>(
        withReuseIdentifier identifier: String,
        for indexPath: IndexPath,
        asType: (TypedCell) -> Void
    ) -> UICollectionViewCell {
        let cell = dequeueReusableCell(withReuseIdentifier: identifier, for: indexPath)
        if let typedCell = cell as? TypedCell {
            asType(typedCell)
        }
        return cell
    }
}
