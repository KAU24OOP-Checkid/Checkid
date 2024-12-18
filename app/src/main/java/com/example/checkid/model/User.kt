import com.example.checkid.model.UsageStatsAdapter

open class  User(
    val id: String,
    var pw: String,
    var partnerId: String?
)

class ChildUser(id: String, pw: String) : User(id, pw, null) {
    var applicationList: MutableList<UsageStatsAdapter> = mutableListOf()  // 자녀가 사용하는 애플리케이션 리스트
}

class ParentUser(id: String, pw: String) : User(id, pw, null) {
    var reportTime : Int? = null
}
