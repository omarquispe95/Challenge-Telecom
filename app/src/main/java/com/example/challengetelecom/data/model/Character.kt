package com.example.challengetelecom.data.model

import android.os.Parcel
import android.os.Parcelable

data class Character (
    val id: Int,
    val name: String?,
    val status: String?,
    val species: String?,
    val type: String?,
    val gender: String?,
    val origin: Info?,
    val location: Info?,
    val image: String?,
    val episode: List<String>?,
    val url: String?,
    val created: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Info::class.java.classLoader),
        parcel.readParcelable(Info::class.java.classLoader),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(status)
        parcel.writeString(species)
        parcel.writeString(type)
        parcel.writeString(gender)
        parcel.writeParcelable(origin, flags)
        parcel.writeParcelable(location, flags)
        parcel.writeString(image)
        parcel.writeStringList(episode)
        parcel.writeString(url)
        parcel.writeString(created)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Character

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    companion object CREATOR : Parcelable.Creator<Character> {
        override fun createFromParcel(parcel: Parcel): Character {
            return Character(parcel)
        }

        override fun newArray(size: Int): Array<Character?> {
            return arrayOfNulls(size)
        }
    }

}