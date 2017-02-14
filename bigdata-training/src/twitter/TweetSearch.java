package twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * ツイート検索クラス.
 *
 * @author Yu
 *
 */
public class TweetSearch {

	/** 日付フォーマット */
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	/**
	 * ツイートを検索します.
	 *
	 * @param args
	 *            引数
	 * @throws TwitterException
	 *             ツイッター例外
	 */
	public static void main(String[] args) {

		if (args.length < 1) {
			// 引数なしの場合
			System.out.println("検索文字列を引数で指定してください");
			return;
		}
		// 初期化
		Twitter twitter = new TwitterFactory().getInstance();
		Query query = new Query();

		PrintWriter pw = null;
		try {
			File file = new File("tweets.tsv");
			// 追記で書き込む
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

			// 検索ワードの数だけ検索する
			for (String arg : args) {
				// 検索ワードをセット
				query.setQuery(arg);
				// 日本のツイートに絞る
				query.setLang("ja");
				// 1度のリクエストで取得するTweetの数（100が最大）
				query.setCount(100);

				// 検索実行
				QueryResult result = twitter.search(query);

				// 検索結果の数だけ繰り返す
				for (Status tweet : result.getTweets()) {

					// 検索した情報をtweets.tsvに出力する
					// ユーザー表示名
					pw.print(tweet.getUser().getScreenName());
					pw.print("\t");
					// ツイート日時
					pw.print(sdf.format(tweet.getCreatedAt()));
					pw.print("\t");
					// ツイート本文(改行はエスケープ)
					pw.print(tweet.getText().replaceAll("\n", "\\\\n"));
					pw.println();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

	}
}