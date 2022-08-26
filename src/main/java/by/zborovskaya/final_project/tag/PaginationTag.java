package by.zborovskaya.final_project.tag;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * The type Pagination tag.
 */
public class PaginationTag extends TagSupport {
    private static final Logger logger = LogManager.getLogger();

    private final int PAGES_FIRST = 1;

    private final int PAGES_PREV = 2;

    private final int PAGES_NEXT = 2;

    private final int PAGES_LAST = 1;

    private boolean showAllPrev;

    private boolean showAllNext;

    private static final String DISABLED_LINK = "<li><a class=\"page-link disabled\">...</a></li>";

    private int currentPage;
    private int lastPage;
    private String url;

    /**
     * Sets current page.
     *
     * @param currentPage the current page
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Sets last page.
     *
     * @param lastPage the last page
     */
    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int doStartTag() throws JspTagException {
        showAllPrev = (PAGES_FIRST + PAGES_PREV + 1) >= currentPage;
        showAllNext = currentPage + PAGES_NEXT >= lastPage - PAGES_LAST;
        logger.log(Level.INFO, showAllPrev + " " + showAllNext);
        printPagination();
        return SKIP_BODY;
    }

    private void printPagination() throws JspTagException{
        JspWriter writer = pageContext.getOut();
        try {
            int prevPage = currentPage - 1 > 0 ? currentPage - 1 : 1;
            writer.write("<ul class=\"pagination\">");
            writer.write(getLinkElement(prevPage, "&lt; Prev"));

            if (showAllPrev) {
                for (int i = 1; i <= currentPage - 1; i++) {
                    writer.write(getLinkElement(i, String.valueOf(i)));
                }

            } else {
                for (int i = 1; i <= PAGES_FIRST; i++) {
                    writer.write(getLinkElement(i, String.valueOf(i)));
                }
                writer.write(DISABLED_LINK);
                for (int i = currentPage - PAGES_PREV; i <= currentPage - 1; i++) {
                    writer.write(getLinkElement(i, String.valueOf(i)));
                }
            }

            writer.write(String.format("<li class=\"active page-item\"><a class=\"page-link\" href=\"%s&currentPage=%d\">%d</a></li>",
                    url, currentPage, currentPage));

            if (showAllNext) {
                for (int i = currentPage + 1; i <= lastPage; i++) {
                    writer.write(getLinkElement(i, String.valueOf(i)));
                }
            } else {
                for (int i = currentPage + 1; i <= currentPage + PAGES_NEXT; i++) {
                    writer.write(getLinkElement(i, String.valueOf(i)));
                }
                writer.write(DISABLED_LINK);
                for (int i = lastPage - PAGES_LAST + 1; i <= lastPage; i++) {
                    writer.write(getLinkElement(i, String.valueOf(i)));
                }
            }
            int nextPage = Math.min(currentPage + 1, lastPage);
            writer.write(getLinkElement(nextPage, "Next &gt;"));
            writer.write("</ul>");
        } catch (IOException e) {
            logger.log(Level.INFO,"Exception in a pagination tag");
            throw new JspTagException("Exception in a pagination tag", e);
        }
    }

    private String getLinkElement(int i, String text){
        logger.log(Level.INFO, "link");
        return String.format("<li class=\"page-item\"><a class=\"page-link\" href=\"%s&currentPage=%d\">%s</a></li>", url, i, text);
    }
    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }
}
