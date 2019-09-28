package `in`.pintrest.imageloader.data

class ImageUrlModel {

    lateinit var id:String
    lateinit var imgurl:String
    constructor(id: String,imgurl:String) {
        this.id = id
        this.imgurl = imgurl
    }
    constructor()
}