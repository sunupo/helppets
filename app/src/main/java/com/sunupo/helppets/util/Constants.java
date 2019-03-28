package com.sunupo.helppets.util;

public class Constants
{
//	public static final String HOST="sun95.320.io";
//	public static final String PORT="47793";
//	http://192.168.43.109:34098/
//	public static final String HOST="192.168.43.109";
	public static final String PORT="34098";
//public static final String HOST="192.168.0.105";
//	public static final String HOST="172.22.106.192";
//	 172.22.106.192
//	public static final String PORT="34098";
//    public static final String PORT="8899";
//    public static final String HOST="114.116.167.1";
public static final String HOST="192.168.0.139";


	public static final String httpip="http://"+HOST+":"+PORT+"/laf";
	public static final String IMAGE_ROOT_PATH=httpip+"/dynamicpicture";
	public static final String LOGIN_NAME="Login_Name";
	public static final String[] NAVIGATION_TITLE={"首页","关注","我的"};
	public static final String[] HOME_TAB={"收藏","广场","同城"};
//	public static final String[] MINE_TAB={"动态","喜欢","关注"};
	public static final String[] MINE_TAB={"动态","关注"};
	public static final String LoginInfo="LoginInfo";
	public static final String COLOR_DARK="#FF5C5C";
	public static final String COLOR_LIGHT="#aaaaaa";

	public static final String APP_KEY="mgb7ka1nmd87g";
	public static final String APP_SECRET="a6ro0cyYDJeZo";
	public static final String ADMIN_TOKEN="Sj2hNeLvrRUvFhqGmzBimuoSw0INgwM5mk/dXz49nWrz8AUbSzBeTEu50jptoDf7iW5IvnCbgI2hgqEXOGkSeQ==";

	public static final String access_token="24.474defbb33833fe6195e7286f5b70111.2592000.1556348840.282335-15876386";//baidu api

	public static final String dt1="暴恐违禁";
	public static final String dt2="文本色情";
	public static final String dt3="政治敏感";
	public static final String dt4="恶意推广";
	public static final String dt5="低俗辱骂";
	public static final String dt6="低质灌水";

	public static final String[] dtArr={dt1,dt2,dt3,dt4,dt5,dt6};


	public static String TEST_JSON = "{\n" +
			"\t\"code\": 1000,\n" +
			"\t\"message\": \"查看评论成功\",\n" +
			"\t\"data\": {\n" +
			"\t\t\"total\": 3,\n" +
			"\t\t\"list\": [{\n" +
			"\t\t\t\t\"id\": 42,\n" +
			"\t\t\t\t\"nickName\": \"程序猿\",\n" +
			"\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
			"\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
			"\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
			"\t\t\t\t\"replyTotal\": 1,\n" +
			"\t\t\t\t\"createDate\": \"三分钟前\",\n" +
			"\t\t\t\t\"replyList\": [{\n" +
			"\t\t\t\t\t\"nickName\": \"沐風\",\n" +
			"\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
			"\t\t\t\t\t\"id\": 40,\n" +
			"\t\t\t\t\t\"commentId\": \"42\",\n" +
			"\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
			"\t\t\t\t\t\"status\": \"01\",\n" +
			"\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
			"\t\t\t\t}]\n" +
			"\t\t\t},\n" +
			"\t\t\t{\n" +
			"\t\t\t\t\"id\": 41,\n" +
			"\t\t\t\t\"nickName\": \"设计狗\",\n" +
			"\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
			"\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
			"\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
			"\t\t\t\t\"replyTotal\": 1,\n" +
			"\t\t\t\t\"createDate\": \"一天前\",\n" +
			"\t\t\t\t\"replyList\": [{\n" +
			"\t\t\t\t\t\"nickName\": \"沐風\",\n" +
			"\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
			"\t\t\t\t\t\"commentId\": \"41\",\n" +
			"\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
			"\t\t\t\t\t\"status\": \"01\",\n" +
			"\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
			"\t\t\t\t}]\n" +
			"\t\t\t},\n" +
			"\t\t\t{\n" +
			"\t\t\t\t\"id\": 40,\n" +
			"\t\t\t\t\"nickName\": \"产品喵\",\n" +
			"\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
			"\t\t\t\t\"content\": \"笨蛋自以为聪明，聪明人才知道自己是笨蛋。\",\n" +
			"\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
			"\t\t\t\t\"replyTotal\": 0,\n" +
			"\t\t\t\t\"createDate\": \"三天前\",\n" +
			"\t\t\t\t\"replyList\": []\n" +
			"\t\t\t}\n" +
			"\t\t]\n" +
			"\t}\n" +
			"}";

//{
//	"refresh_token": "25.cce66d81f1a3aa7f15269acbb8de506a.315360000.1869116840.282335-15876386",
//	"expires_in": 2592000,
//	"session_key": "9mzdDxM603hPeBi5eDUvhMzRAbn+HtFIgJUWkN+fV+vIywq\/hO8rpmliyveUzDtZv8X\/pfj3rURfDDtN7Y56h2fUDjWj4w==",
//	"access_token": "24.474defbb33833fe6195e7286f5b70111.2592000.1556348840.282335-15876386",
//	"scope": "public vis-antiporn_antiporn_v2 vis-classify_watermark brain_gif_antiporn vis-classify_terror brain_all_scope vis-starface_public_person solution_face brain_antiporn brain_antiterror vis-classify_\u6076\u5fc3\u56fe\u8bc6\u522b\u670d\u52a1 brain_politician brain_imgquality_general brain_watermark brain_public brain_disgust brain_antispam_spam wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test\u6743\u9650 vis-classify_flower lpq_\u5f00\u653e cop_helloScope ApsMis_fangdi_permission smartapp_snsapi_base iop_autocar oauth_tp_app smartapp_smart_game_openapi oauth_sessionkey smartapp_swanid_verify smartapp_opensource_openapi",
//	"session_secret": "124d51f8562db964e4bdf26621c6c1ff"
//}
}
