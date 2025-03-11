package tokiokaability.auction.HTTPInfo;

import org.apache.http.NameValuePair;

import java.time.LocalDateTime;
import java.util.List;


public class InquiryInfo extends BaseInfo {

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionDate() {
        return questionDate;
    }

    public void setQuestionDate(String questionDate) {
        this.questionDate = questionDate;
    }

    public String getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(String answerDate) {
        this.answerDate = answerDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getDisplayFlg() {
        return this.displayFlg;
    }

    public void setDisplayFlg(String displayFlg) {
        this.displayFlg = displayFlg;
    }

    private String question = "";
    private String answer = "";
    private String updateDate = "";
    private String questionDate = "";
    private String answerDate = "";
    private String displayFlg = "";

    public static final String TAG_STATUS = "Status";
    public static final String TAG_QUESTION = "Question";
    public static final String TAG_ANSWER = "Answer";
    public static final String TAG_UPDATE_DATE = "UpdateDate";
    public static final String TAG_QUESTION_DATE = "QuestionDate";
    public static final String TAG_ANSWER_DATE = "AnswerDate";
    public static final String TAG_DISPLAY_FLG = "DisplayFlg";

    public InquiryInfo() {
        super();
    }

    public InquiryInfo(String data) throws XMLParserException, InvalidDataException, ServerErrorException {
        List<NameValuePair> parserResult;

        try {
            parserResult = BaseInfo.parse(data);

            for (NameValuePair nvp : parserResult) {
                if (nvp.getName().equalsIgnoreCase(TAG_STATUS)) {
                    if (Integer.parseInt(nvp.getValue()) == -1)
                        throw new ServerErrorException("Server Error");
                    else
                        this.setStatus(Integer.parseInt(nvp.getValue()));
                } else if (nvp.getName().equalsIgnoreCase(TAG_QUESTION)) {
                    this.setQuestion(nvp.getValue());
                } else if (nvp.getName().equalsIgnoreCase(TAG_ANSWER)) {
                    this.setAnswer(nvp.getValue());
                } else if (nvp.getName().equalsIgnoreCase(TAG_UPDATE_DATE)) {
                    this.setUpdateDate(nvp.getValue());
                } else if (nvp.getName().equalsIgnoreCase(TAG_QUESTION_DATE)) {
                    this.setQuestionDate(nvp.getValue());
                } else if (nvp.getName().equalsIgnoreCase(TAG_ANSWER_DATE)) {
                    this.setAnswerDate(nvp.getValue());
                } else if (nvp.getName().equalsIgnoreCase(TAG_DISPLAY_FLG)) {
                    this.setDisplayFlg(nvp.getValue());
                }
            }
        } catch (XMLParserException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (ServerErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InvalidDataException(ex.getMessage());
        }
    }
}
