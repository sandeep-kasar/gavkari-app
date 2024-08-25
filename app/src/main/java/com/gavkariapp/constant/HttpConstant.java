package com.gavkariapp.constant;


import com.gavkariapp.BuildConfig;

public class HttpConstant {
    
    /*
    Api vesion
    */
    
    private static final String API_V1 = "api/v1/";

    /**
     * base url for api and image access
     */
    //public static final String BASE_URL = "http://10.0.2.2/gawakariapp/index.php/";//localhost
    public static final String BASE_URL = BuildConfig.HOST;
    public static final String BASE_EVENT_DOWNLOAD_URL = BuildConfig.MEDIA+"event/";
    public static final String BASE_NEWS_DOWNLOAD_URL = BuildConfig.MEDIA+"news/";
    public static final String BASE_AVATAR_DOWNLOAD_URL = BuildConfig.MEDIA+"avatar/";
    public static final String BASE_BUYSALE_DOWNLOAD_URL = BuildConfig.MEDIA+"buy-sale/";
    public static final String BASE_BANNER_DOWNLOAD_URL = BuildConfig.MEDIA+"banner/";

    /**
     * headers
     */
    public static final String JSON_TYPE = "Content-Type:application/json";
    public static final String CACHE_CONTROL = "Cache-Control:no-cache";

    /**
     * Operations.
     */

    // upload
    public static final String UPLOAD_EVENT = API_V1 + "event/upload";
    public static final String UPLOAD_NEWS = API_V1 + "news/upload";
    public static final String UPLOAD_AVATAR = API_V1 + "user/upload";
    public static final String UPLOAD_SALE = API_V1 + "buysale/upload";

    /**
     * User login
     */
    public static final String SIGN_IN = API_V1 + "user/login";
    public static final String SIGN_OUT = API_V1 + "user/logout";
    public static final String SIGN_UP = API_V1 + "user/register";
    public static final String GET_STATE = API_V1 + "user/state";
    public static final String GET_DISTRICT = API_V1 + "user/district";
    public static final String GET_TALUKA = API_V1 + "user/taluka";
    public static final String GET_VILLAGE = API_V1 + "user/village";
    public static final String PROFILE_EDIT = API_V1 + "user/update";
    public static final String REQUEST_OTP = API_V1 + "user/request/otp";
    public static final String VERIFY_MOBILE = API_V1 + "user/verify/mobile";


    /**
     * User Data
     */
    public static final String MY_AD = API_V1 + "user/myevent/";
    public static final String MYAD_EDIT = API_V1 + "user/myevent/edit";
    public static final String MY_AD_DELETE = API_V1 + "user/myevent/delete/";
    public static final String EDIT_AD = API_V1 + "user/mysaleads/edit";
    public static final String MY_SALE_AD = API_V1 + "user/saleads/";
    public static final String MY_SALE_AD_DELETE = API_V1 + "user/saleads/delete/";
    public static final String MY_SALE_AD_SOLD = API_V1 + "user/saleads/sold/";
    public static final String GET_BUY_SALE_FAV = API_V1 + "user/fav";
    public static final String EDIT_NEWS= API_V1 + "user/mynews/edit";
    public static final String MY_NEWS= API_V1 + "user/mynews/";
    public static final String MY_NEWS_DELETE = API_V1 + "user/mynews/delete/";
    public static final String MY_NOTIFICATION = API_V1 + "user/notification/";

    /**
     * Event Data
     */
    public static final String MY_VILLAGE = API_V1 + "event/myvillage";
    public static final String CREATE_EVENT = API_V1 + "event/create";
    public static final String GET_EVENT_MEDIA= API_V1 + "event/photos/";
    public static final String GET_EVENT_MATTER= API_V1 + "event/load/matter";

    /**
     * Directory Data
     */
    public static final String GET_DIRECTORY = API_V1 + "directory/myvillage/";
    public static final String CREATE_DIR = API_V1 + "directory/create";
    public static final String GET_MY_DIR = API_V1 + "directory/my_dir/";


    /**
     * News Data
     */
    public static final String GET_NEWS_MATTER= API_V1 + "news/load/matter";
    public static final String CREATE_NEWS= API_V1 + "news/create";
    public static final String GET_NEWS_MEDIA= API_V1 + "news/photos/";
    public static final String NEWS_VILLAGES = API_V1 + "news/village";


    /**
     * News Data
     */
    public static final String GET_BUY_SALE = API_V1 + "buysale/all";
    public static final String GET_BUY_SALE_ANIMAL = API_V1 + "buysale/animal";
    public static final String GET_BUY_SALE_MACHINE = API_V1 + "buysale/machine";
    public static final String GET_BUY_SALE_EQUIPMENT = API_V1 + "buysale/equipment";
    public static final String CREATE_AD = API_V1 + "buysale/create";
    public static final String FAV_DELETE = API_V1 + "buysale/add/fav";
    public static final String GET_BUYSALE_MEDIA= API_V1 + "buysale/photos";


    /**
     * Notification Data
     */
    public static final String NOTIFICATION_STATUS = API_V1 + "user/notification/status";
    public static final String NOTIFICATION_ALL = API_V1 + "notification/all";


    /**
     * Api code.
     */
    public static final int SUCCESS = 1;
    public static final int EMPTY_REQUEST = -1;
    public static final int FIELD_IS_EMPTY = -2;
    public static final int FAIL_TO_INSERT = -3;
    public static final int DUPLICATE_DATA = -4;
    public static final int NO_DATA_AVAILABLE = -5;

}
