package tokiokaability.auction24.view;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
 
public class LimitedEditText extends EditText
{
	public static final int UNLIMITED = -100;
	private int maxLength;
	
    public LimitedEditText(Context context, AttributeSet attrs, int defStyle) 
    {
        super(context, attrs, defStyle);
        initComponents(attrs);
    }
    
    public LimitedEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initComponents(attrs);
    }
    
    public LimitedEditText(Context context)
    {
        super(context);
        initComponents(null);
    }
     
    private void initComponents(AttributeSet attrs)
    {
    	if(attrs!=null) {
    		//attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "maxLength")
    		maxLength = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLength", UNLIMITED);
    	}
    	
    	super.addTextChangedListener(new  TextWatcher(){
    		
    		String text;
            int beforeCursorPosition = 0;
            boolean isMaxLength = false;
            
            @Override
            public void afterTextChanged(Editable s) {
            	if (isMaxLength == true && s.toString().length() >= text.length()) {
	            	removeTextChangedListener(this);
	            	LimitedEditText.this.setText(text);
	                LimitedEditText.this.setSelection(beforeCursorPosition);
	                addTextChangedListener(this);
	                isMaxLength = false;
            	}
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            	try {
					int lengthBytes = s.toString().getBytes("Shift-JIS").length;

					text = s.toString();
			        beforeCursorPosition = start;
			        
					if (maxLength != UNLIMITED && lengthBytes == maxLength) {
				        isMaxLength = true;
					}
            	} catch (UnsupportedEncodingException e) {
				}
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {}
        });
    }
     
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
}