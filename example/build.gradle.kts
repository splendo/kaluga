import com.splendo.kaluga.example.plugin.EmbeddingMode

plugins {
    id("com.splendo.kaluga.example.plugin")
    id("com.splendo.kaluga.plugin")
}

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
