package jar.enums;

import static com.codeborne.selenide.Selenide.*;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

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

	public SelenideElement createTarget(String target) {
		return createElement(target, 0);
	};
	public abstract SelenideElement createElement(String target, int num);

}
