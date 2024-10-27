package suites;

import data.StaticData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.FilterListPO;

@Epic("Service Suite")
@Feature("Post-Service Actions")
public class ServiceSuite extends BaseSuiteClassNew {

    @Test
    @Description("Delete filter lists, created by autotests")
    public void clearFilterLists(){
        AuthorizationPO auth = new AuthorizationPO();
        FilterListPO filterList = new FilterListPO();
        auth.login(StaticData.supportDefaultUser);
        filterList.gotoFilterListSection();
        filterList.clearFilterLists();
    }

}
