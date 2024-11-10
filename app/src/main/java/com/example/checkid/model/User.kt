import com.example.checkid.model.AppUsage

/*
목적: 자녀의 앱 사용 통계를 조회할 수 있는 화면(Fragment)을 처리하는 코드입니다.
기능: MainActivity_kid와 유사하게 앱 사용 권한을 확인하고, 권한이 부여되면 앱 사용 시간을 가져옵니다.
 */

open class User(
    val id: String,
    var pw: String
)

class ChildUser(id: String, pw: String) : User(id, pw) {
    var parent: ParentUser? = null
    var applicationList: MutableList<AppUsage> = mutableListOf()  // 자녀가 사용하는 애플리케이션 리스트
    var totalUsageTime: Long = 0 // 총 사용 시간 (초 단위)

    // 자식의 앱 사용 시간 계산
    fun addUsage(appName: String, usageTime: Long) {
        applicationList.add(AppUsage(appName, usageTime))
        totalUsageTime += usageTime
    }
}

class ParentUser(id: String, pw: String) : User(id, pw) {
    var child: ChildUser? = null  // 부모가 자식에 대한 참조를 유지
}
