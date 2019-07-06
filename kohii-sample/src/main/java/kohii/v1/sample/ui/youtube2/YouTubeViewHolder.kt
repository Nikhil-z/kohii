/*
 * Copyright (c) 2019 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kohii.v1.sample.ui.youtube2

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.google.api.services.youtube.model.Video
import kohii.v1.Playback
import kohii.v1.PlaybackEventListener
import kohii.v1.sample.R
import kohii.v1.sample.common.BaseViewHolder
import kohii.v1.sample.svg.GlideApp

@Suppress("MemberVisibilityCanBePrivate")
class YouTubeViewHolder(
  parent: ViewGroup,
  layoutId: Int,
  val fragmentManager: FragmentManager
) : BaseViewHolder(parent, layoutId),
    PlaybackEventListener {

  val container = itemView.findViewById(R.id.container) as FrameLayout
  val thumbnail = itemView.findViewById(R.id.thumbnail) as ImageView
  val videoTitle = itemView.findViewById(R.id.videoTitle) as TextView

  var playback: Playback<*>? = null

  init {
    // container.id = ViewCompat.generateViewId()
  }

  override fun bind(item: Any?) {
    super.bind(item)
    (item as? Video)?.apply {
      val lowResThumb = this.snippet.thumbnails.medium.url
      val thumbRequest = Glide.with(itemView)
          .load(lowResThumb)
      val highResThumb = this.snippet.thumbnails.maxres.url
      GlideApp.with(itemView)
          .load(highResThumb)
          .thumbnail(thumbRequest)
          .fitCenter()
          .into(thumbnail)

      videoTitle.text = this.snippet.title
    }
  }

  override fun onEnd(playback: Playback<*>) {
    thumbnail.isVisible = true
  }

  override fun beforePlay(playback: Playback<*>) {
    thumbnail.isVisible = false
  }

  override fun afterPause(playback: Playback<*>) {
    thumbnail.isVisible = true
  }

  override fun onRecycled(success: Boolean) {
    super.onRecycled(success)
    playback = null
  }
}