package com.sedsoftware.yaptalker.presentation.feature.activetopics.adapters

interface ActiveTopicsItemClickListener {

  fun goToSelectedTopic(triple: Triple<Int, Int, Int>)
}
