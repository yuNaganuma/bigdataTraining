package twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
			File file = new File("tweets.txt");
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
				System.out.println("「" + arg + "」でヒットした件数 : " + result.getTweets().size());

				// 検索結果の数だけ繰り返す
				for (Status tweet : result.getTweets()) {

					System.out.println("「" + arg + "」でヒットしました : " + tweet.getText());
					// 検索結果をtweets.txtに書きこむ
					pw.println(tweet.getText());
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