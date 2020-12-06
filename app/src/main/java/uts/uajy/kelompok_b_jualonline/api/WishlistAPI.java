package uts.uajy.kelompok_b_jualonline.api;

public class WishlistAPI {

//    public static final String ROOT_URL   = "https://180709738.000webhostapp.com/";
    public static final String ROOT_URL   = "https://penjualanonline.apitubespbp.xyz/";
    public static final String ROOT_API   = ROOT_URL+ "api/";
    public static final String URL_IMAGE  = ROOT_URL+"images/";

    //    Route::post('wishlist','Api\WishlistController@store'); // create
    //    method POST create
    public static final String URL_CREATE_WISHLIST = ROOT_API + "wishlist";

    //    Route::get('wishlist','Api\WishlistController@index'); //read all
    //    method GET read all
    public static final String URL_LOGIN = ROOT_API + "wishlist";

    //    Route::get('wishlist/{id}','Api\WishlistController@show'); //read selected by id
    //    method GET read selected by ID
    public static final String URL_GET = ROOT_API + "wishlist/";

    //    Route::post('wishlist/update/{id}','Api\WishlistController@update'); //update
    //    method POST update
    public static final String URL_UPDATE = ROOT_API + "wishlist/update/";

    //    Route::post('wishlist/delete/{id}','Api\WishlistController@destroy'); //delete
    //    method POST by ID
    public static final String URL_DELETE = ROOT_API + "wishlist/delete/";
}