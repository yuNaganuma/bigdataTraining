package twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * ツイート収集クラス.
 *
 * @author 長沼佑
 *
 */
public class TweetCollecter {

	/** 日付フォーマット */
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	/**
	 * ツイートを収集します.
	 *
	 * @param args
	 *            引数
	 * @throws TwitterException
	 *             ツイッター例外
	 */
	public static void main(String[] args) throws TwitterException {

		// TwitterStreamのインスタンス作成
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		// Listenerを登録
		twitterStream.addListener(new Listener());
		// 実行
		twitterStream.sample();
	}

	static class Listener extends StatusAdapter {

		public void onStatus(Status status) {

			if ("ja".equals(status.getLang())) {
				// 日本のツイートに絞る

				PrintWriter pw = null;
				try {
					// プロジェクト配下に出力
					File file = new File("tweets.tsv");
					pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					// 取り出した情報をtweets.tsvに出力する
					// ユーザー表示名
					pw.print(status.getUser().getScreenName());
					pw.print("\t");
					// ツイート日時
					pw.print(sdf.format(status.getCreatedAt()));
					pw.print("\t");
					// ツイート本文(改行はエスケープ)
					pw.print(status.getText().replaceAll("\n", "\\\\n"));
					pw.println();

				} catch (IOException e) {
					e.printStackTrace();
				} finally {

					if (pw != null) {
						pw.close();
					}
				}
			}
		}
	}
}