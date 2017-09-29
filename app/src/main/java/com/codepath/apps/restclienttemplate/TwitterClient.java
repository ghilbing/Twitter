package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.format.DateUtils;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "eyBE2tuCFyiPwllzXzHUmGyxU";       // Change this
	public static final String REST_CONSUMER_SECRET = "NxD5wuSF82JvvnPizqvg4L5x7ultgATKDoEV5ADCvMw2K2k9yJ"; // Change this
	public static final int T_X_PAGE = 25;

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}
	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getHomeTimeline(String maxId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
			params.put("count", T_X_PAGE);
			params.put("since", 1);

		client.get(apiUrl, params, handler);
	}

	public void getUser(String name, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("include_entities", "false");
		params.put("screen_name", name);
		client.get(apiUrl, params, handler);
	}

	public void getMentions(String maxId, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", T_X_PAGE);
		params.put("max_id", maxId);
		params.put("include_rts", 1);
		client.get(apiUrl, params, handler);
	}

	public void getUserTimeline(String username, String maxId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("max_id", maxId);
		params.put("screen_name", username);
		params.put("count", T_X_PAGE);
		client.get(apiUrl, params, handler);
	}

	public void getCredentials(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		RequestParams params = new RequestParams();
		params.put("skip_status", 1);
		params.put("include_entities", "false");
		client.get(apiUrl, params, handler);
	}

	public void tweet(String status, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");

		RequestParams params = new RequestParams();
		params.put("status", status);

		client.post(apiUrl, params, handler);
	}


	// Got this from https://gist.github.com/nesquena/f786232f5ef72f6e10a7
	public static String getTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
		String relativeDate = "";

		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return relativeDate;
	}

	public void getFollowers(JsonHttpResponseHandler repsonseHandler, String screenName) {
		String apiUrl = getApiUrl("followers/list.json");
		//specify the params
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		params.put("count", 200);
		getClient().get(apiUrl, params, repsonseHandler);
	}


	public void getFriends(JsonHttpResponseHandler repsonseHandler, String screenName) {
		String apiUrl = getApiUrl("friends/list.json");
		//specify the params
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		params.put("count", 200);
		getClient().get(apiUrl, params, repsonseHandler);
	}

	public void postUpdate(String body, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		client.post(apiUrl, params, handler);
	}

	public void composeTweet(AsyncHttpResponseHandler repsonseHandler, String tweetBody, Boolean isReply, long id) {
		String apiUrl = getApiUrl("statuses/update.json");
		//specify the params
		RequestParams params = new RequestParams();
		params.put("status", tweetBody);
		if (isReply) {
			params.put("in_reply_to_status_id", id);
		}
		getClient().post(apiUrl, params, repsonseHandler);
	}



	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
