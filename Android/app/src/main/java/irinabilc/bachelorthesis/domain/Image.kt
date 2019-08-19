package irinabilc.bachelorthesis.domain
import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Image (
    val image: String
) : Parcelable