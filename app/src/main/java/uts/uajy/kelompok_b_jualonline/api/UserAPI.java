package uts.uajy.kelompok_b_jualonline.api;

public class UserAPI {

//    public static final String ROOT_URL   = "https://180709738.000webhostapp.com/";
    public static final String ROOT_URL   = "https://penjualanonline.apitubespbp.xyz/";
    public static final String ROOT_API   = ROOT_URL+ "api/";
    public static final String URL_IMAGE  = ROOT_URL+"images/";

    //    Route::post('user/register','Api\UserController@register'); // create
    //    method POST create
    public static final String URL_REGISTER = ROOT_API + "user/register";

    //    Route::post('user/login','Api\UserController@login'); // login
    //    method POST login
    public static final String URL_LOGIN = ROOT_API + "user/login";

    //    Route::get('user','Api\UserController@index'); //read all
    //    method GET read all
    public static final String URL_GET = ROOT_API + "user";

    //    Route::get('user/{id}','Api\UserController@show'); //read selected by id
    //    method GET read selected by ID
    public static final String URL_GET_ID = ROOT_API + "user/";

    //    Route::post('user/update/{id}','Api\UserController@update'); //update
    //    method POST update
    public static final String URL_UPDATE = ROOT_API + "user/update/";

    //    Route::post('user/delete/{id}','Api\UserController@destroy'); //delete
    //    method DELETE
    public static final String URL_DELETE = ROOT_API + "user/delete/";

    //    Route::post('user-verification/{id}','Api\UserController@output'); //update verification
    //    method POST update verification

}
