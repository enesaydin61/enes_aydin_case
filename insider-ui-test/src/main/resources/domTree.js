(function () {
  const highlightColors = [
    '#FF0000', '#00FF00', '#0000FF', '#FFA500',
    '#800080', '#008080', '#FF69B4', '#4B0082',
    '#FF4500', '#2E8B57', '#DC143C', '#4682B4'
  ];

  function isElementVisible(element) {
    if (!element) {
      return false;
    }
    const style = window.getComputedStyle(element);
    const isSpecialInput = element.tagName.toLowerCase() === 'input' &&
        (element.type === 'checkbox' || element.type === 'radio');
    const hasSize = isSpecialInput ? true : (element.offsetWidth > 0
        && element.offsetHeight > 0);
    const isDisplayed = style.visibility !== 'hidden' && style.display !== 'none';
    const hasPointerEvents = style.pointerEvents !== 'none';
    return hasSize && isDisplayed && hasPointerEvents;
  }

  function isInteractiveElement(element) {
    if (!element) {
      return false;
    }
    const interactiveElements = new Set([
      'a', 'button', 'input', 'select', 'textarea', 'summary', 'p', 'span',
      'div'
    ]);
    return interactiveElements.has(element.tagName?.toLowerCase())
        || element.hasAttribute('onclick');
  }

  function getRelativeXPath(element, ancestor) {
    let segments = [];
    let current = element;
    while (current && current !== ancestor) {
      let index = 1;
      let sibling = current.previousElementSibling;
      while (sibling) {
        if (sibling.tagName === current.tagName) {
          index++;
        }
        sibling = sibling.previousElementSibling;
      }
      segments.unshift(`${current.tagName.toLowerCase()}[${index}]`);
      current = current.parentElement;
    }
    return segments.length ? "/" + segments.join("/") : "";
  }

  function getSmartXPath(element) {
    if (!element || element.nodeType !== Node.ELEMENT_NODE) {
      return '';
    }

    const tag = element.tagName.toLowerCase();

    if (element.id) {
      return `//${tag}[@id='${element.id}']`;
    }

    if (element.name) {
      const nameSelector = `[name='${element.name}']`;
      if (document.querySelectorAll(nameSelector).length === 1) {
        return `//${tag}[@name='${element.name}']`;
      }
    }

    if (element.className) {
      const classAttr = element.className.trim();
      if (classAttr !== "") {
        const classSelector = `[class='${classAttr}']`;
        if (document.querySelectorAll(classSelector).length === 1) {
          return `//${tag}[@class='${classAttr}']`;
        }
      }
    }

    const text = getElementText(element);
    if (text && text.length > 0) {
      const xpathByText = `//${tag}[normalize-space(text())='${text.trim()}']`;
      const result = document.evaluate(`count(${xpathByText})`, document, null,
          XPathResult.NUMBER_TYPE, null);
      if (result.numberValue === 1) {
        return xpathByText;
      }
    }

    let ancestor = element.parentElement;
    let levelsUp = 0;
    while (ancestor && levelsUp < 1) {
      let baseXPath = "";
      const ancestorTag = ancestor.tagName.toLowerCase();
      if (ancestor.id) {
        baseXPath = `//${ancestorTag}[@id='${ancestor.id}']`;
      } else if (ancestor.name) {
        const nameSelector = `[name='${ancestor.name}']`;
        if (document.querySelectorAll(nameSelector).length === 1) {
          baseXPath = `/${ancestorTag}[@name='${ancestor.name}']`;
        }
      }

      if (baseXPath) {
        const relativePath = getRelativeXPath(element, ancestor);
        if (relativePath) {
          return baseXPath + relativePath;
        }
      }
      ancestor = ancestor.parentElement;
      levelsUp++;
    }

    const segments = [];
    let current = element;
    while (current && current.nodeType === Node.ELEMENT_NODE) {
      let segment;
      const currentTag = current.tagName.toLowerCase();
      if (current.id) {
        segment = `/${currentTag}[@id='${current.id}']`;
        segments.unshift(segment);
        break;
      } else {
        let index = 1;
        let sibling = current.previousElementSibling;
        while (sibling) {
          if (sibling.nodeName === current.nodeName) {
            index++;
          }
          sibling = sibling.previousElementSibling;
        }
        segment = `${currentTag}[${index}]`;
        segments.unshift(segment);
        current = current.parentElement;
      }
    }

    if (segments[0].startsWith(`//${tag}[@id=`)) {
      return segments.join('/');
    } else {
      return '/' + segments.join('/');
    }
  }

  function highlightElement(element, index) {
    if (!element || !element.style) {
      return;
    }
    let color = highlightColors[index % highlightColors.length];
    let bgColor = color + "1A"; // %10 opaklık

    element.style.outline = "3px solid " + color;
    element.style.backgroundColor = bgColor;
    element.style.transition = "outline 0.2s ease-in-out";

    const label = document.createElement("div");
    label.textContent = index;
    label.style.position = "absolute";
    label.style.background = color;
    label.style.color = "white";
    label.style.padding = "2px 5px";
    label.style.borderRadius = "4px";
    label.style.fontSize = "12px";
    label.style.fontWeight = "bold";
    label.style.zIndex = "9999";

    const rect = element.getBoundingClientRect();
    label.style.top = (window.scrollY + rect.top) + "px";
    label.style.left = (window.scrollX + rect.left + rect.width - 20) + "px";

    document.body.appendChild(label);

    setTimeout(() => {
      label.remove();
    }, 500);
  }

  function removeHighlightEffect(element) {
    if (!element || !element.style) {
      return;
    }
    element.style.outline = "";
    element.style.backgroundColor = "";
  }

  function getElementText(element) {
    if (!element) {
      return "";
    }

    if (element.tagName === "select") {
      return "";
    }
    return element.innerText?.trim() || element.textContent?.trim() || "";
  }

  function findInteractiveElements(focusHighlightIndex) {
    let elements = [...document.querySelectorAll(
        "a, button, input, select, textarea, summary, p, span, div, [onclick]"
    )];

    document.querySelectorAll("iframe").forEach(iframe => {
      try {
        let iframeDoc = iframe.contentDocument || iframe.contentWindow.document;
        if (!iframeDoc) {
          return;
        }
        iframeDoc.querySelectorAll(
            "a, button, input, select, textarea, summary, p, span, div, [onclick]"
        ).forEach(element => {
          elements.push(element);
        });
      } catch (e) {
        console.warn("Erişilemeyen iframe atlandı:", iframe);
      }
    });

    let interactiveElements = elements.filter(el => {
      if (!isInteractiveElement(el)) {
        return false;
      }
      if (!isElementVisible(el)) {
        return false;
      }
      return !(el.tagName.toLowerCase() === 'div' && !el.id);
    });

    interactiveElements.forEach((el, index) => {
      if (focusHighlightIndex === -1 || focusHighlightIndex === index) {
        highlightElement(el, index);
      }
    });

    setTimeout(() => {
      interactiveElements.forEach((el, index) => {
        if (focusHighlightIndex === -1 || focusHighlightIndex === index) {
          removeHighlightEffect(el);
        }
      });
    }, 500);

    return interactiveElements
    .filter(el => {
      const text = getElementText(el);
      return el.id || el.name || text;
    })
    .map(el=> {
      try {
        const tagName = el.tagName.toLowerCase();
        const elementData = {
          tagName: tagName,
          xpath: getSmartXPath(el),
          text: getElementText(el),
          className: el.className || "",
          id: el.id || "",
          name: el.name || ""
        };

        if (tagName === "select") {
          elementData.options = Array.from(el.options).map(opt => ({
            text: opt.text
          }));
        }
        return elementData;
      } catch (error) {
        console.error("Element işlenirken hata oluştu:", error);
        return null;
      }
    })
    .filter(item => item !== null);
  }

  return findInteractiveElements(-1);
})();
