package jar.operator;

import static com.codeborne.selenide.Selenide.*;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

/**
 * 要素Enum<p>
 * 文字列と配列番号をSelenideElementに変換する処理を持つ。
 * </p>
 *   */
public enum ElementEnum {

	id {
		@Override
		public SelenideElement createElement(String target, int num) {
			return $$(By.id(target)).get(num);
		}
	},
	name {
		@Override
		public SelenideElement createElement(String target, int num) {
			return $$(By.name(target)).get(num);
		}
	},
	className {
		@Override
		public SelenideElement createElement(String target, int num) {
			return $$(By.className(target)).get(num);
		}
	},
	cssSelector {
		@Override
		public SelenideElement createElement(String target, int num) {
			return $$(By.cssSelector(target)).get(num);
		}
	},
	xpath {
		@Override
		public SelenideElement createElement(String target, int num) {
			return $$(By.xpath(target)).get(num);
		}
	},
	linkText {
		@Override
		public SelenideElement createElement(String target, int num) {
			return $$(By.linkText(target)).get(num);
		}
	},
	partialLinkText {
		@Override
		public SelenideElement createElement(String target, int num) {
			return $$(By.partialLinkText(target)).get(num);
		}
	},
	tagname {
		@Override
		public SelenideElement createElement(String target, int num) {
			return $$(By.tagName(target)).get(num);
		}
	},
	unused {

		@Override
		public SelenideElement createElement(String target, int num) {
			return null;
		}
	}
	;

	/** 要素の配列番号が渡されなかった場合、一つ目の要素を取得する。 */
	public SelenideElement createTarget(String target) {
		return createElement(target, 0);
	}

	/** SelenideElement取得処理 */
	public abstract SelenideElement createElement(String target, int num);

}
