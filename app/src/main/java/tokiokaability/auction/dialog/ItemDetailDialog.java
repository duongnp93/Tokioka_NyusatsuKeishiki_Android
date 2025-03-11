package tokiokaability.auction.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tokiokaability.auction.HTTPInfo.ItemInfo;
import tokiokaability.auction.R;
import tokiokaability.auction.activities.ItemDetailActivity;
import tokiokaability.auction.commons.Utilities;

public class ItemDetailDialog extends Dialog {
    Context mContext;
    ImageButton closeBtn;
    ListView lstItemDetail;
    RelativeLayout rootLayout, formBgr;
    ItemInfo itemInfo;
    Typeface yGB, yGR;
    private android.view.View.OnClickListener closeClickListener;
    private DetailInforAdapter detailInfoAdapter;

    public ItemDetailDialog(Activity activity, android.view.View.OnClickListener closeClickListener) {
        super(activity);
        this.closeClickListener = closeClickListener;
        this.setCanceledOnTouchOutside(false);

        this.mContext = activity;
    }

    public void show(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
        if (detailInfoAdapter == null)
            detailInfoAdapter = new DetailInforAdapter(mContext);
        detailInfoAdapter.setItemInfor(itemInfo);
        detailInfoAdapter.notifyDataSetChanged();
        if (lstItemDetail != null) {
            lstItemDetail.smoothScrollToPosition(0);
        }
        this.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_item_detail);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        closeBtn = findViewById(R.id.closeBtn);
        formBgr = findViewById(R.id.formBgr);
        rootLayout = findViewById(R.id.rootLayout);
        lstItemDetail = findViewById(R.id.lstDialogDetail);
        lstItemDetail.setAdapter(detailInfoAdapter);
        closeBtn.setOnClickListener(closeClickListener);
        rootLayout.setOnClickListener(this.closeClickListener);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) setFont();
        lstItemDetail.smoothScrollToPosition(0);
    }

    private void setFont() {
        AssetManager am = getContext().getAssets();
        yGB = Typeface.createFromAsset(am, "fonts/YUGOTHB.TTC");
        yGR = Typeface.createFromAsset(am, "fonts/YUGOTHR.TTC");
    }

    public enum DetailItemType {
        SessionNo,
        AuctionDate,
        BoxNo,
        AbsenceState,
        ItemName,
        SeriResult,
        StartPrice,
        LastPrice,
        SeriTime,
        AskingPrice,
        AuctionResult,
        AbsenteePrice,
        Details,
        Brand,
        SellingSort,
        CornerNm,
        SubCat,
        Material,
        Line,
        Model1,
        Model2,
        SerialNo,
        RefPrice,
        Rank,
        Accessories,
        Bless,
        IndividualNo,
        Color,
        Size,
        Instrument,
        Comments,
        Memo,
        QR,
        QR2,
        QR3,
        QR4,
        Size2,
        Size3,
        Size4
    }

    public class DetailItem {
        public DetailItemType rowType;
        public String label;
        public String[] values;
        public int innerRowCount;

        public DetailItem(DetailItemType type, int labelId, int innerRowCount) {
            rowType = type;
            label = mContext.getResources().getString(labelId);
            values = null;
            this.innerRowCount = innerRowCount;
        }
    }

    public final class DetailInforAdapter extends BaseAdapter {

        private Context mContext;
        private ItemInfo itemInfor;
        private ArrayList<DetailItem> rows;
        // 文字一覧の項目数
        private int itemCount;


        public DetailInforAdapter(Context context) {
            mContext = context;
            //this.itemInfor = itemInfor;
            rows = new ArrayList<DetailItem>();
        }


        public void setItemInfor(ItemInfo item) {
            this.itemInfor = item;
            rows = new ArrayList<DetailItem>();
            if(item.getCornerNm().equals("宝石")||item.getItemNo() > 50000){
                rows.add(new DetailItem(DetailItemType.AbsenteePrice, R.string.hoseki_detail_1, 1));
                rows.add(new DetailItem(DetailItemType.StartPrice, R.string.hoseki_detail_2_1, 1));
                rows.add(new DetailItem(DetailItemType.Brand, R.string.hoseki_detail_3, 1));
                rows.add(new DetailItem(DetailItemType.Model1, R.string.hoseki_detail_4, 1));
                rows.add(new DetailItem(DetailItemType.Model2, R.string.hoseki_detail_5, 1));
                rows.add(new DetailItem(DetailItemType.Comments, R.string.hoseki_detail_6, 1));
                rows.add(new DetailItem(DetailItemType.Material, R.string.hoseki_detail_7, 1));
                rows.add(new DetailItem(DetailItemType.IndividualNo, R.string.hoseki_detail_8, 1));
                rows.add(new DetailItem(DetailItemType.Line, R.string.hoseki_detail_9, 1));
                rows.add(new DetailItem(DetailItemType.QR, R.string.hoseki_detail_10, 1));
                rows.add(new DetailItem(DetailItemType.QR2, R.string.hoseki_detail_11, 1));
//                rows.add(new DetailItem(DetailItemType.QR3, R.string.hoseki_detail_12, 1));
//                rows.add(new DetailItem(DetailItemType.QR4, R.string.hoseki_detail_13, 1));
                rows.add(new DetailItem(DetailItemType.Accessories, R.string.hoseki_detail_14, 1));
                rows.add(new DetailItem(DetailItemType.Details, R.string.hoseki_detail_15, 1));
                rows.add(new DetailItem(DetailItemType.Size, R.string.hoseki_detail_16, 1));
                rows.add(new DetailItem(DetailItemType.Size2, R.string.hoseki_detail_17, 1));
//                rows.add(new DetailItem(DetailItemType.Size3, R.string.hoseki_detail_18, 1));
//                rows.add(new DetailItem(DetailItemType.Size4, R.string.hoseki_detail_19, 1));
                rows.add(new DetailItem(DetailItemType.CornerNm, R.string.hoseki_detail_20, 1));
                rows.add(new DetailItem(DetailItemType.SubCat, R.string.hoseki_detail_21, 1));
                rows.add(new DetailItem(DetailItemType.BoxNo, R.string.hoseki_detail_22, 1));
                rows.add(new DetailItem(DetailItemType.SellingSort, R.string.hoseki_detail_23, 1));
                rows.add(new DetailItem(DetailItemType.SeriResult, R.string.hoseki_detail_24, 1));
                rows.add(new DetailItem(DetailItemType.Memo, R.string.detai_row_26, 1));
            }
            else{
                rows.add(new DetailItem(DetailItemType.AbsenteePrice, R.string.detai_row_1, 1));
                rows.add(new DetailItem(DetailItemType.StartPrice, R.string.detai_row_21, 1));
                rows.add(new DetailItem(DetailItemType.Brand, R.string.detai_row_6, 1));
                rows.add(new DetailItem(DetailItemType.Model1, R.string.detai_row_9, 1));
                rows.add(new DetailItem(DetailItemType.Model2, R.string.detai_row_10, 1));
                rows.add(new DetailItem(DetailItemType.Line, R.string.detai_row_8, 1));
                rows.add(new DetailItem(DetailItemType.SerialNo, R.string.detai_row_11, 1));
                rows.add(new DetailItem(DetailItemType.Material, R.string.detai_row_7, 1));
                rows.add(new DetailItem(DetailItemType.Accessories, R.string.detai_row_14, 1));
                rows.add(new DetailItem(DetailItemType.IndividualNo, R.string.detai_row_16, 1));
                rows.add(new DetailItem(DetailItemType.Details, R.string.detai_row_2, 1));
                rows.add(new DetailItem(DetailItemType.Comments, R.string.detai_row_20, 1));
                rows.add(new DetailItem(DetailItemType.Instrument, R.string.detai_row_19, 1));
                rows.add(new DetailItem(DetailItemType.Color, R.string.detai_row_17, 1));
                rows.add(new DetailItem(DetailItemType.Size, R.string.detai_row_18, 1));
                rows.add(new DetailItem(DetailItemType.Rank, R.string.detai_row_13, 1));
                rows.add(new DetailItem(DetailItemType.CornerNm, R.string.detai_row_5, 1));
                rows.add(new DetailItem(DetailItemType.SubCat, R.string.detai_row_23, 1));
                rows.add(new DetailItem(DetailItemType.BoxNo, R.string.detai_row_3, 1));
                rows.add(new DetailItem(DetailItemType.SellingSort, R.string.detai_row_4, 1));
                rows.add(new DetailItem(DetailItemType.SeriResult, R.string.detai_row_22, 1));
                rows.add(new DetailItem(DetailItemType.Memo, R.string.detai_row_26, 1));
            }
            itemCount = rows.size();
        }

        @Override
        public int getItemViewType(int position) {
            switch (rows.get(position).rowType) {
                case SessionNo:
                case AuctionDate:
                case BoxNo:
                case ItemName:
                case SeriResult:
                case StartPrice:
                case LastPrice:
                case SeriTime:

                case Details:

                case AskingPrice:
                case AuctionResult:
                case Brand:
                case SellingSort:
                case CornerNm:
                case SubCat:
                case Material:
                case Line:
                case Model1:
                case Model2:
                case SerialNo:
                case RefPrice:
                case Rank:
                case Accessories:
                case Bless:
                case IndividualNo:
                case Color:
                case Size:
                case Instrument:
                case Comments:
                case Memo:
                case QR:
                case QR2:
                case QR3:
                case QR4:
                case Size2:
                case Size3:
                case Size4:
                case AbsenteePrice:
                    return 0;
                case AbsenceState:
                    return 4;
                default:
                    return 0;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 5;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView lblLabel = null;
            TextView lblValue = null;


            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.view_item_detail_rowinfo_simple, parent, false);

            }

            lblLabel = (TextView) convertView.findViewById(R.id.lblLabel);
            if (lblLabel != null) {
                lblLabel.setText(rows.get(position).label);
            }

            switch (rows.get(position).rowType) {
                case ItemName:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getItemName() != null)
                        lblValue.setText(itemInfor.getItemName());
                    else
                        lblValue.setText("");
                    break;

                case SeriResult:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getResultName() != null)
                        lblValue.setText(itemInfor.getResultName());
                    else
                        lblValue.setText("");
                    break;

                case StartPrice:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getStartPrice() != null)
                        lblValue.setText(Utilities.formatPrice(this.mContext, itemInfor.getStartPrice()));
                    else
                        lblValue.setText("- 円");
                    break;

                case LastPrice:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getLastPrice() != null)
                        lblValue.setText(Utilities.formatPrice(mContext, itemInfor.getLastPrice()));
                    else
                        lblValue.setText("- 円");
                    break;
                case AbsenteePrice:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);

                    if (itemInfor != null) {

                        String tmp = itemInfor.getAbsenceBidPrice();
                        if (itemInfor.getAbsenceFlag() == 2) {
                            lblValue.setText(mContext.getString(R.string.item_detail_none_nobid));
                        } else {
                            lblValue.setText((tmp == null || tmp.equals("") || tmp.equals("0")) ? mContext.getString(R.string.item_detail_none_string) : Utilities.formatPrice(mContext, tmp)); // 不在入札価格
                        }

                    } else
                        lblValue.setText("");
                    break;

                case Details:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getDetails() != null) {
                        lblValue.setText(itemInfor.getDetails());
                    } else
                        lblValue.setText("");
                    break;
                case BoxNo:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getBoxNo() != null && itemInfor.getBoxSubNo() != null) {
                        lblValue.setText(itemInfor.getBoxNo() + "-" + itemInfor.getBoxSubNo());
                    } else
                        lblValue.setText("");
                    break;

                case SellingSort:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSellingSort() != null) {
                        lblValue.setText(itemInfor.getSellingSort());
                    } else
                        lblValue.setText("");
                    break;
                case CornerNm:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getCornerNm() != null) {
                        lblValue.setText(itemInfor.getCornerNm());
                    } else
                        lblValue.setText("");
                    break;
                case SubCat:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSubCat() != null) {
                        lblValue.setText(itemInfor.getSubCat());
                    } else
                        lblValue.setText("");
                    break;
                case Brand:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getBrand() != null) {
                        lblValue.setText(itemInfor.getBrand());
                    } else
                        lblValue.setText("");
                    break;
                case Material:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getMaterial() != null) {
                        lblValue.setText(itemInfor.getMaterial());
                    } else
                        lblValue.setText("");
                    break;
                case Line:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getLine() != null) {
                        lblValue.setText(itemInfor.getLine());
                    } else
                        lblValue.setText("");
                    break;
                case RefPrice:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getRefPrice() != null) {
                        lblValue.setText(Utilities.formatPrice(this.mContext, itemInfor.getRefPrice()));
                    } else
                        lblValue.setText("0 円");
                    break;
                case Model1:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getModel1() != null) {
                        lblValue.setText(itemInfor.getModel1());
                    } else
                        lblValue.setText("");
                    break;
                case Model2:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getModel2() != null) {
                        lblValue.setText(itemInfor.getModel2());
                    } else
                        lblValue.setText("");
                    break;
                case SerialNo:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSerialNo() != null) {
                        lblValue.setText(itemInfor.getSerialNo());
                    } else
                        lblValue.setText("");
                    break;
                case QR:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getQR() != null) {
                        lblValue.setText(itemInfor.getQR());
                    } else
                        lblValue.setText("");
                    break;
                case QR2:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getQR2() != null) {
                        lblValue.setText(itemInfor.getQR2());
                    } else
                        lblValue.setText("");
                    break;
                case QR3:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getQR3() != null) {
                        lblValue.setText(itemInfor.getQR3());
                    } else
                        lblValue.setText("");
                    break;
                case QR4:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getQR4() != null) {
                        lblValue.setText(itemInfor.getQR4());
                    } else
                        lblValue.setText("");
                    break;
                case Rank:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getRank() != null) {
                        lblValue.setText(itemInfor.getRank());
                    } else
                        lblValue.setText("");
                    break;
                case Accessories:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getAccessories() != null) {
                        lblValue.setText(itemInfor.getAccessories());
                    } else
                        lblValue.setText("");
                    break;
                case Bless:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getBless() != null) {
                        lblValue.setText(itemInfor.getBless());
                    } else
                        lblValue.setText("");
                    break;
                case IndividualNo:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getIndividualNo() != null) {
                        lblValue.setText(itemInfor.getIndividualNo());
                    } else
                        lblValue.setText("");
                    break;
                case Color:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getColor() != null) {
                        lblValue.setText(itemInfor.getColor());
                    } else
                        lblValue.setText("");
                    break;
                case Size:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSize() != null) {
                        lblValue.setText(itemInfor.getSize());
                    } else
                        lblValue.setText("");
                    break;
                case Size2:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSize2() != null) {
                        lblValue.setText(itemInfor.getSize2());
                    } else
                        lblValue.setText("");
                    break;
                case Size3:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSize3() != null) {
                        lblValue.setText(itemInfor.getSize3());
                    } else
                        lblValue.setText("");
                    break;
                case Size4:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSize4() != null) {
                        lblValue.setText(itemInfor.getSize4());
                    } else
                        lblValue.setText("");
                    break;
                case Instrument:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getInstrument() != null) {
                        lblValue.setText(itemInfor.getInstrument());
                    } else
                        lblValue.setText("");
                    break;
                case Comments:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getComments() != null) {
                        lblValue.setText(itemInfor.getComments());
                    } else
                        lblValue.setText("");
                    break;
                case Memo:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getMemo() != null) {
                        lblValue.setText(itemInfor.getMemo());
                    } else
                        lblValue.setText("");
                    break;
            }


            lblLabel.setTypeface(yGR);
            lblValue.setTypeface(yGR);
            return convertView;
        }

        // 文字一覧の項目数
        @Override
        public int getCount() {
            return itemCount;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


    }
}
