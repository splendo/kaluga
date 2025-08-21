import com.splendo.kaluga.example.plugin.EmbeddingMode

plugins {
    id("com.splendo.kaluga.example.plugin")
    id("com.splendo.kaluga.plugin")
}

// override or append this version to load a custom version of kaluga from maven central, combined with settings
version = libs.versions.kaluga.get()

kaluga {
    when (val embeddingMode = embedding.embeddingMode) {
        is EmbeddingMode.MavenLocal -> {
            includeMavenLocal = true
        }
        is EmbeddingMode.MavenRepo -> {
            additionalMavenRepos.add(embeddingMode.repoUrl)
        }
        else -> {}
    }
}
