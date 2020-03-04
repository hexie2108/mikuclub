package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.adapter.PostSubmitImageAdapter;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.MediaDelegate;
import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.javaBeans.parameters.CreatePostParameters;
import org.mikuclub.app.javaBeans.response.SingleMedia;
import org.mikuclub.app.javaBeans.response.SinglePost;
import org.mikuclub.app.javaBeans.response.WpError;
import org.mikuclub.app.javaBeans.response.baseResource.Category;
import org.mikuclub.app.javaBeans.response.baseResource.ImagePreview;
import org.mikuclub.app.javaBeans.response.baseResource.Media;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.storage.CategoryPreferencesUtils;
import org.mikuclub.app.utils.AlertDialogUtils;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.custom.MyTextWatcher;
import org.mikuclub.app.utils.file.ImageCompression;
import org.mikuclub.app.utils.file.LocalResourceIntent;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class PostSubmitActivity extends AppCompatActivity
{

        /* 静态变量 Static variable */
        public static final int TAG = 17;

        private static final String INTENT_POST_ID = "post_id";

        /* 变量 local variable */
        //分类id矩阵
        private ArrayList<ArrayList<Integer>> categoriesMatrix = new ArrayList<>();
        //分类名称数组
        private ArrayList<String> categoriesName;
        //储存被选中分类id数组的位置
        private Integer selectedCategoryPosition;
        private MediaDelegate mediaDelegate;
        private PostDelegate postDelegate;
        //列表适配器
        private PostSubmitImageAdapter recyclerViewAdapter;
        //列表数据
        private List<ImagePreview> recyclerDataList;

        //用来判断是新建文章 还是编辑旧文章
        private int postId;
        //在编辑旧文章的时候 才会用到
        private Post post;


        /* 组件 views */
        private RecyclerView recyclerView;

        private TextInputLayout inputTitleLayout;
        private TextInputLayout inputCategoryLayout;

        private TextInputLayout inputDescriptionLayout;
        private TextInputLayout inputDownload1Layout;
        private TextInputLayout inputDownload2Layout;
        private TextInputLayout inputBilibiliLayout;

        private TextInputEditText inputTitle;
        private TextInputEditText inputCategory;
        private TextInputEditText inputSourceName;
        private TextInputEditText inputDescription;
        private TextInputEditText inputDownload1;
        private TextInputEditText inputDownload2;
        private TextInputEditText inputPassword1;
        private TextInputEditText inputPassword2;
        private TextInputEditText inputUnzipPassword1;
        private TextInputEditText inputUnzipPassword2;
        private TextInputEditText inputTag;
        private TextInputEditText inputBilibili;

        private MaterialButton buttonUpload;
        private MaterialButton buttonSubmit;
        private MaterialButton buttonDraft;

        private AlertDialog categoryDialog;
        private AlertDialog progressDialog;
        private AlertDialog getPostRetryDialog;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_post_submit);

                //绑定组件
                Toolbar toolbar = findViewById(R.id.toolbar);
                recyclerView = findViewById(R.id.recycler_view);

                inputTitleLayout = findViewById(R.id.input_title_layout);
                inputCategoryLayout = findViewById(R.id.input_category_layout);
                inputDescriptionLayout = findViewById(R.id.input_description_layout);
                inputDownload1Layout = findViewById(R.id.input_download1_layout);
                inputDownload2Layout = findViewById(R.id.input_download2_layout);
                inputBilibiliLayout = findViewById(R.id.input_bilibili_layout);

                inputTitle = findViewById(R.id.input_title);
                inputCategory = findViewById(R.id.input_category);
                inputSourceName = findViewById(R.id.input_source_name);
                inputDescription = findViewById(R.id.input_description);
                inputDownload1 = findViewById(R.id.input_download1);
                inputDownload2 = findViewById(R.id.input_download2);
                inputPassword1 = findViewById(R.id.input_password1);
                inputPassword2 = findViewById(R.id.input_password2);
                inputUnzipPassword1 = findViewById(R.id.input_unzip_password1);
                inputUnzipPassword2 = findViewById(R.id.input_unzip_password2);

                inputBilibili = findViewById(R.id.input_bilibili);

                buttonUpload = findViewById(R.id.button_upload);
                buttonSubmit = findViewById(R.id.button_submit);
                progressDialog = AlertDialogUtils.createProgressDialog(this, false, false);

                /*目前没有使用*/
                inputTag = findViewById(R.id.input_tag);
                buttonDraft = findViewById(R.id.button_draft);
                /*===============*/

                //如果有传递文章id 说明是在编辑旧投稿
                postId = getIntent().getIntExtra(INTENT_POST_ID, 0);

                mediaDelegate = new MediaDelegate(TAG);
                postDelegate = new PostDelegate(TAG);
                //创建空列表
                recyclerDataList = new ArrayList<>();

                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示标题栏返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        //如果是在编辑旧文章
                        if (postId > 0)
                        {
                                //更换标题名
                                actionBar.setTitle(ResourcesUtils.getString(R.string.edit));
                        }
                }

                //初始化表单
                initForm();

                //初始化图片预览列表
                initRecyclerView();

                //如果文章id大于0说明是在编辑旧文章
                if (postId > 0)
                {
                        //准备获取文章信息, 之后用信息填写表单
                        prepareGetPost();
                        //获取文章
                        getPostData();
                }


        }

        /**
         * 准备获取文章信息
         */
        private void prepareGetPost()
        {

                //弹窗确认按钮点击事件监听
                DialogInterface.OnClickListener positiveClickListener = (dialog, which) -> {
                        //重试请求
                        getPostData();
                };
                //弹窗取消按钮点击事件监听
                DialogInterface.OnClickListener negativeClickListener = (dialog, which) -> {
                        //关闭当前页面
                        finish();
                };
                //创建重试弹窗
                getPostRetryDialog = AlertDialogUtils.createConfirmDialog(this, ResourcesUtils.getString(R.string.post_get_by_id_error_message), null, true, true, ResourcesUtils.getString(R.string.retry), positiveClickListener, ResourcesUtils.getString(R.string.cancel), negativeClickListener);


        }

        /**
         * 获取文章信息
         */
        private void getPostData()
        {
                //显示进度条
                progressDialog.show();

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                //获取文章数据
                                post = ParserUtils.fromJson(response, SinglePost.class).getBody();
                                //用数据填充表单
                                fillForm();
                        }

                        @Override
                        public void onError(WpError wpError)
                        {
                                super.onError(wpError);
                                //弹出确认窗口 允许用户重试
                                getPostRetryDialog.show();
                        }

                        @Override
                        public void onHttpError()
                        {
                                onError(null);
                        }

                        @Override
                        public void onFinally()
                        {
                                //隐藏进度条弹窗
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onCancel()
                        {
                                //显示重试弹窗
                                getPostRetryDialog.show();
                        }
                };
                postDelegate.getPost(httpCallBack, postId);


        }


        /**
         * 在编辑旧投稿的情况
         * 用文章数据预先填满表单
         */
        private void fillForm()
        {

                //获取文章的元数据
                Post.Metadata metadata = post.getMetadata();

                //修复标题中可能存在的被html转义的特殊符号
                String title = GeneralUtils.unescapeHtml(post.getTitle().getRendered());
                inputTitle.setText(title);

                //设置分类名
                findCategoryById();

                //获取来源说明
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getSource_name()))
                {
                        inputSourceName.setText(metadata.getSource_name().get(0));
                }
                //获取文章描述
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getContent()))
                {
                        inputDescription.setText(metadata.getContent().get(0));
                }
                //获取下载地址
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getDown()))
                {
                        inputDownload1.setText(metadata.getDown().get(0));
                }
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getDown2()))
                {
                        inputDownload2.setText(metadata.getDown2().get(0));
                }
                //获取访问密码
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getPassword()))
                {
                        inputPassword1.setText(metadata.getPassword().get(0));
                }
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getPassword2()))
                {
                        inputPassword2.setText(metadata.getPassword2().get(0));
                }
                //获取解压密码
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getUnzip_password()))
                {
                        inputUnzipPassword1.setText(metadata.getUnzip_password().get(0));
                }
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getUnzip_password2()))
                {
                        inputUnzipPassword2.setText(metadata.getUnzip_password2().get(0));
                }
                //获取B站地址
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getBilibili()))
                {
                        inputBilibili.setText(metadata.getBilibili().get(0));
                }

                //获取图片预览地址
                ImagePreview imagePreview;
                for (int i = 0; i < metadata.getPreviews().size(); i++)
                {
                        //防止图片地址数组超过范围
                        if (i < metadata.getImages_src().size())
                        {
                                //创建图片预览类
                                imagePreview = new ImagePreview();
                                imagePreview.setId(metadata.getPreviews().get(i));
                                imagePreview.setSource_url(metadata.getImages_src().get(i));
                                imagePreview.setAlreadySubmitted(true);
                                //添加进列表
                                recyclerDataList.add(imagePreview);
                        }
                }
                //更新列表显示
                recyclerViewAdapter.notifyDataSetChanged();

        }

        /**
         * 初始化表单
         */
        private void initForm()
        {
                //绑定内容变化监听器
                initFormInputWatcher();

                //创建分类选择弹窗
                categoryDialog = createCategorySelectDialog();

                //绑定分类按钮监听器
                inputCategory.setOnClickListener(v -> {
                        //显示分类弹窗
                        categoryDialog.show();
                });

                //绑定上传图片按钮
                buttonUpload.setOnClickListener(v -> {
                                //上传图片动作
                                uploadImageAction();
                        }
                );

                buttonSubmit.setOnClickListener(v -> {
                        //提交动作
                        submitAction();
                });


        }

        /**
         * 初始化recyclerView列表
         * init recyclerView
         */
        private void initRecyclerView()
        {

                //创建适配器
                recyclerViewAdapter = new PostSubmitImageAdapter(this, recyclerDataList);

                //创建列表主布局
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                //配置列表
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE, false, false, null);


        }

        /**
         * 上传图片动作
         */
        private void uploadImageAction()
        {
                if (recyclerDataList.size() < GlobalConfig.MAX_IMAGE_PREVIEWS_COUNT)
                {
                        //启动相册选择图片
                        LocalResourceIntent.startActionForResultToGetImage(this);
                }
                else
                {
                        //提示错误
                        ToastUtils.shortToast(ResourcesUtils.getString(R.string.image_preview_max_count_error));
                }

        }

        /**
         * 绑定表单内容变化监听
         */
        private void initFormInputWatcher()
        {
                //输入过程中的即使检测
                MyTextWatcher titleWatcher = MyTextWatcher.createMyTextWatcherForEmpty(inputTitle, inputTitleLayout, ResourcesUtils.getString(R.string.input_empty_error));
                MyTextWatcher categoryWatcher = MyTextWatcher.createMyTextWatcherForEmpty(inputCategory, inputCategoryLayout, ResourcesUtils.getString(R.string.input_empty_error));
                MyTextWatcher descriptionWatcher = MyTextWatcher.createMyTextWatcherForEmpty(inputDescription, inputDescriptionLayout, ResourcesUtils.getString(R.string.input_empty_error));

                MyTextWatcher downloadWatcher = new MyTextWatcher(() -> {
                        String text;
                        //获取下载点1
                        if (inputDownload1.getText() != null)
                        {
                                text = inputDownload1.getText().toString().trim();
                                //如果内容不为空
                                if (!text.isEmpty())
                                {
                                        //取消错误
                                        inputDownload1Layout.setError(null);
                                }
                        }
                        //获取下载点2
                        if (inputDownload2.getText() != null)
                        {
                                text = inputDownload2.getText().toString().trim();
                                //如果内容不为空
                                if (!text.isEmpty())
                                {
                                        //取消错误
                                        inputDownload2Layout.setError(null);
                                }
                        }
                });
                MyTextWatcher bilibiliWatcher = new MyTextWatcher(() -> {
                        if (inputBilibili.getText() != null)
                        {
                                String text = inputBilibili.getText().toString().trim();
                                //如果内容不为空
                                if (!text.isEmpty())
                                {
                                        //取消错误
                                        inputBilibiliLayout.setError(null);
                                }
                        }
                });

                inputTitle.addTextChangedListener(titleWatcher);
                inputCategory.addTextChangedListener(categoryWatcher);
                inputDescription.addTextChangedListener(descriptionWatcher);
                inputDownload1.addTextChangedListener(downloadWatcher);
                inputDownload2.addTextChangedListener(downloadWatcher);
                inputBilibili.addTextChangedListener(bilibiliWatcher);

                //输入完成后的内容修正
                //去除空格
                View.OnFocusChangeListener focusChangeListener = (v, hasFocus) -> {
                        //如果失去焦点 并且是 输入框组件
                        if (!hasFocus && v instanceof TextInputEditText)
                        {
                                TextInputEditText editText = (TextInputEditText) v;
                                if (editText.getText() != null)
                                {
                                        //去除左右空格
                                        String text = editText.getText().toString().trim();
                                        editText.setText(text);
                                }

                        }
                };

                inputTitle.setOnFocusChangeListener(focusChangeListener);
                inputSourceName.setOnFocusChangeListener(focusChangeListener);
                inputDescription.setOnFocusChangeListener(focusChangeListener);

                inputPassword1.setOnFocusChangeListener(focusChangeListener);
                inputPassword2.setOnFocusChangeListener(focusChangeListener);
                inputUnzipPassword1.setOnFocusChangeListener(focusChangeListener);
                inputUnzipPassword2.setOnFocusChangeListener(focusChangeListener);
                inputBilibili.setOnFocusChangeListener(focusChangeListener);

                //下载栏专用 解析百度盘链接
                View.OnFocusChangeListener focusChangeListenerForDownload = (v, hasFocus) -> {
                        //如果失去焦点 并且是 输入框组件
                        if (!hasFocus && v instanceof TextInputEditText)
                        {
                                TextInputEditText editText = (TextInputEditText) v;
                                if (editText.getText() != null)
                                {
                                        //去除左右空格
                                        String text = editText.getText().toString().trim();
                                        //去除所有的空格和空白符号和 全角冒号和 逗号
                                        text = text.replace(" ", "").replaceAll("\\s*", "").replace("：", ":").replace("，", ",");

                                        String linkName = "链接:";
                                        //如果存在 "链接: "  这个字符串
                                        int linkNameIndex = text.indexOf(linkName);
                                        if (linkNameIndex != -1)
                                        {
                                                //移除字符串 和之前的任何东西
                                                text = text.substring(linkNameIndex + linkName.length());
                                        }

                                        String passwordName = "提取码:";
                                        //如果存在 "提取码:   这个字符串
                                        int passwordNameIndex = text.indexOf(passwordName);
                                        if (passwordNameIndex != -1)
                                        {

                                                //提取出密码
                                                String password = text.substring(passwordNameIndex + passwordName.length());
                                                //从下载地址中 移除提取码 字符串和后续密码内容
                                                text = text.substring(0, passwordNameIndex);

                                                //如果是下载点1
                                                if (editText == inputDownload1)
                                                {
                                                        inputPassword1.setText(password);
                                                }
                                                //如果是下载点2
                                                else if (editText == inputDownload2)
                                                {
                                                        inputPassword2.setText(password);
                                                }
                                        }

                                        editText.setText(text);
                                }
                        }
                };

                inputDownload1.setOnFocusChangeListener(focusChangeListenerForDownload);
                inputDownload2.setOnFocusChangeListener(focusChangeListenerForDownload);

        }

        /**
         * 上传图片
         */
        private void updateImage(Uri imageUri)
        {

                //解析图片uri , 生成压缩过的副本到 应用缓存文件夹里
                File imageFile = ImageCompression.compressFileIfTooLarge(this, imageUri);
                //如果图片为空, 就提示错误
                if (imageFile == null)
                {
                        ToastUtils.shortToast("无法创建对应图片的压缩版本");
                }
                else
                {
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                @Override
                                public void onSuccess(String response)
                                {
                                        Media media = ParserUtils.fromJson(response, SingleMedia.class).getBody();
                                        ImagePreview imagePreview = new ImagePreview();
                                        imagePreview.setId(media.getId());
                                        imagePreview.setSource_url(media.getMedia_details().getSizes().getThumbnail().getSource_url());
                                        recyclerDataList.add(imagePreview);
                                        //recyclerViewAdapter.notifyItemInserted(recyclerDataList.size()-1);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                }


                                @Override
                                public void onFinally()
                                {
                                        progressDialog.dismiss();
                                }

                                @Override
                                public void onCancel()
                                {
                                        progressDialog.dismiss();
                                }
                        };

                        progressDialog.show();
                        mediaDelegate.uploadImage(httpCallBack, imageFile);

                }


        }


        /**
         * 提交动作
         */
        private void submitAction()
        {
                //首先隐藏键盘, 触发focus监听器 来移除内容的左右空格
                KeyboardUtils.hideKeyboard(getCurrentFocus());

                //依次检查每个输入框
                boolean hasError = false;
                String title = null;
                String sourceName = null;
                String description = null;
                String download1 = null;
                String download2 = null;
                String password1 = null;
                String password2 = null;
                String unzipPassword1 = null;
                String unzipPassword2 = null;
                String bilibili = null;

                //检查标题栏
                if (inputTitle.getText() != null)
                {
                        title = inputTitle.getText().toString();
                        //标题替换默认方括号 改成全角括号
                        title = title.replace("[", "【").replace("]", "】");
                        if (title.isEmpty())
                        {
                                inputTitleLayout.setError(ResourcesUtils.getString(R.string.input_empty_error));
                                hasError = true;
                        }
                }

                //检查分类栏
                if (inputCategory.getText() != null)
                {
                        String text = inputCategory.getText().toString();
                        //如果分类栏未选择 或者 对应的分类位置未设置
                        if (text.isEmpty() || selectedCategoryPosition == null)
                        {
                                inputCategoryLayout.setError(ResourcesUtils.getString(R.string.input_empty_error));
                                hasError = true;
                        }
                }

                //获取来源说明
                if (inputSourceName.getText() != null)
                {
                        sourceName = inputSourceName.getText().toString();
                }
                //检查描述
                if (inputDescription.getText() != null)
                {
                        description = inputDescription.getText().toString();
                        if (description.isEmpty())
                        {
                                inputDescriptionLayout.setError(ResourcesUtils.getString(R.string.input_empty_error));
                                hasError = true;
                        }
                }
                //检查下载栏1
                if (inputDownload1.getText() != null)
                {
                        download1 = inputDownload1.getText().toString();
                        //如果不是空, 并且不是磁链地址, 并且 不是 有效的网址
                        if (!download1.isEmpty() && !download1.contains("magnet:") && !GeneralUtils.isValidUrl(download1))
                        {
                                inputDownload1Layout.setError(ResourcesUtils.getString(R.string.download_link_error));
                                hasError = true;
                        }
                }
                //检查下载栏2
                if (inputDownload2.getText() != null)
                {
                        download2 = inputDownload2.getText().toString();
                        //如果不是空, 并且不是磁链地址, 并且 不是 有效的网址
                        if (!download2.isEmpty() && !download2.contains("magnet:") && !GeneralUtils.isValidUrl(download2))
                        {
                                inputDownload2Layout.setError(ResourcesUtils.getString(R.string.download_link_error));
                                hasError = true;
                        }
                }

                //获取密码1  和 密码2
                if (inputPassword1.getText() != null)
                {
                        password1 = inputPassword1.getText().toString();
                }
                if (inputPassword2.getText() != null)
                {
                        password2 = inputPassword2.getText().toString();
                }
                //获取解压密码1  和 解压密码2
                if (inputUnzipPassword1.getText() != null)
                {
                        unzipPassword1 = inputUnzipPassword1.getText().toString();
                }
                if (inputUnzipPassword2.getText() != null)
                {
                        unzipPassword2 = inputUnzipPassword2.getText().toString();
                }

                //检查B站地址
                if (inputBilibili.getText() != null)
                {
                        bilibili = inputBilibili.getText().toString();
                        //如果不是空, 并且不是磁链地址, 并且 不是 有效的网址
                        if (!bilibili.isEmpty())
                        {
                                //查询"AV"字符的位置
                                int biliIndex = bilibili.indexOf(GlobalConfig.ThirdPartyApplicationInterface.BILIBILI_AV);
                                //如果av字符存在
                                if (biliIndex != -1)
                                {
                                        //提取出AV号 : av123456
                                        bilibili = bilibili.substring(biliIndex);
                                        //去除av号后面可能存在的多余后缀
                                        String[] extraSymbol = {"/", "?", "#"};
                                        for (int i = 0; i < extraSymbol.length; i++)
                                        {
                                                biliIndex = bilibili.indexOf(extraSymbol[i]);
                                                if (biliIndex != -1)
                                                {
                                                        bilibili = bilibili.substring(0, biliIndex);
                                                }
                                        }
                                        //更新显示的文本
                                        inputBilibili.setText(bilibili);
                                }
                                //如果地址不包含av号
                                else
                                {
                                        inputBilibiliLayout.setError(ResourcesUtils.getString(R.string.bilibili_link_error));
                                        hasError = true;
                                }
                        }
                }

                // 如果发现了表单存在错误
                if (hasError)
                {
                        ToastUtils.shortToast("当前表单里存在错误, 请根据错误提示进行更改");
                }
                //如果未上传任何图片预览
                else if (recyclerDataList.isEmpty())
                {
                        ToastUtils.shortToast("请至少上传一张图片");
                }

                //如果未发现错误
                else
                {

                        //创建参数
                        CreatePostParameters parameters = new CreatePostParameters();
                        CreatePostParameters.Meta meta = new CreatePostParameters.Meta();
                        meta.setSource_name(sourceName);
                        meta.setContent(description);
                        meta.setDown(download1);
                        meta.setDown2(download2);
                        meta.setPassword(password1);
                        meta.setPassword2(password2);
                        meta.setUnzip_password(unzipPassword1);
                        meta.setUnzip_password2(unzipPassword2);
                        meta.setBilibili(bilibili);

                        //创建图片预览的id数组
                        ArrayList<String> previews = new ArrayList<>();
                        for (int i = 0; i < recyclerDataList.size(); i++)
                        {
                                previews.add(String.valueOf(recyclerDataList.get(i).getId()));
                        }
                        meta.setPreviews(previews);

                        parameters.setTitle(title);
                        parameters.setCategories(categoriesMatrix.get(selectedCategoryPosition));
                        parameters.setMeta(meta);

                        //发送提交请求
                        sendSubmitRequest(parameters);

                }


                //去除中文字符
                //text = GeneralUtils.replaceChineseCharacters(text);
        }


        /**
         * 发送请求
         *
         * @param parameters
         */
        private void sendSubmitRequest(CreatePostParameters parameters)
        {

                //显示进度条
                progressDialog.show();

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                //如果投稿提交成功
                                ToastUtils.shortToast("提交成功");
                                //清空列表 避免图片附件被退出时删除
                                recyclerDataList.clear();
                                //跳转到投稿管理页
                                PostManageActivity.startAction(PostSubmitActivity.this);
                                finish();
                        }

                        @Override
                        public void onError(WpError wpError)
                        {
                                super.onError(wpError);
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onHttpError()
                        {
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onCancel()
                        {
                                progressDialog.dismiss();
                        }
                };

                //如果是创建文章
                if (postId == 0)
                {
                        postDelegate.createPost(httpCallBack, parameters);
                }
                //如果是更新旧文章
                else
                {
                        postDelegate.updatePost(httpCallBack, parameters, postId);
                }


        }


        /**
         * 创建分类选择弹窗
         *
         * @return
         */
        private AlertDialog createCategorySelectDialog()
        {

                createCategoriesName();

                AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this);
                builder.setCancelable(true);
                builder.setTitle(ResourcesUtils.getString(R.string.select_category));
                //创建按钮列表 , -1 代表没有预选中选项
                builder.setSingleChoiceItems(categoriesName.toArray(new String[0]), -1, (dialog, position) -> {
                        //更改分类栏内容
                        inputCategory.setText(categoriesName.get(position));
                        //更新选中分类id的位置
                        selectedCategoryPosition = position;
                        //隐藏弹窗
                        dialog.dismiss();
                });

                return builder.create();
        }

        /**
         * 创建分类名称列表 (包含主分类和子分类)
         * 创建分类id矩阵
         *
         * @return
         */
        private void createCategoriesName()
        {

                categoriesName = new ArrayList<>();

                List<Category> categories = CategoryPreferencesUtils.getCategory();
                ArrayList<Integer> categoriesId;
                List<Category> childCategories;

                for (int i = 0; i < categories.size(); i++)
                {
                        //如果有子分类
                        childCategories = categories.get(i).getChildren();
                        if (!GeneralUtils.listIsNullOrHasEmptyElement(childCategories))
                        {

                                for (int j = 0; j < childCategories.size(); j++)
                                {
                                        //添加到分类名称数组
                                        categoriesName.add(categories.get(i).getTitle() + " - " + childCategories.get(j).getTitle());
                                        //创建分类id数组, 用来储存主分类的id 和子分类的id
                                        categoriesId = new ArrayList<>(Arrays.asList(categories.get(i).getId(), childCategories.get(j).getId()));
                                        categoriesMatrix.add(categoriesId);
                                }
                        }
                        //如果没有子分类
                        else
                        {
                                //添加到分类名称数组
                                categoriesName.add(categories.get(i).getTitle());
                                //创建分类id数组, 只储存主分类的id
                                categoriesId = new ArrayList<>(Collections.singletonList(categories.get(i).getId()));
                                categoriesMatrix.add(categoriesId);
                        }

                }
        }

        /**
         * 根据分类id号, 找到对应分类在矩阵里的位置 和 设置对应的分类名
         */
        private void findCategoryById()
        {

                ArrayList<Integer> subCategoryIds;

                for (int i = 0; i < post.getCategories().size(); i++)
                {
                        for (int j = 0; j < categoriesMatrix.size(); j++)
                        {

                                subCategoryIds = categoriesMatrix.get(j);
                                //如果文章分类id 匹配到 子分类的id, 说明找到了
                                if (post.getCategories().get(i).equals(subCategoryIds.get(subCategoryIds.size() - 1)))
                                {
                                        //保存找到的分类数组的位置
                                        selectedCategoryPosition = j;
                                        //设置分类名
                                        inputCategory.setText(categoriesName.get(j));
                                        //退出循环
                                        j = categoriesMatrix.size();
                                        i = post.getCategories().size();
                                }
                        }
                }

        }

        /**
         * 退出确认窗口
         */
        private void showExitConfirmDialog()
        {
                //创建退出确认窗口
                //只有在点击确认的情况 才会退出当前活动
                DialogInterface.OnClickListener positiveClickListener = (dialog, which) -> PostSubmitActivity.super.onBackPressed();
                AlertDialog exitConfirmDialog = AlertDialogUtils.createConfirmDialog(this, ResourcesUtils.getString(R.string.confim_exit_title), ResourcesUtils.getString(R.string.confirm_exit_content), true, false, ResourcesUtils.getString(R.string.confirm), positiveClickListener, ResourcesUtils.getString(R.string.cancel), null);
                exitConfirmDialog.show();

        }

        /**
         * 通知服务器删除未提交的图片文件
         */
        private void deleteUnSubmitImage()
        {
                for (int i = 0; i < recyclerDataList.size(); i++)
                {
                        //如果是未提交的图片文件
                        if (!recyclerDataList.get(i).isAlreadySubmitted())
                        {
                                //发送删除请求
                                mediaDelegate.deleteMediaInBackground(recyclerDataList.get(i).getId());
                        }
                }
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
        {
                super.onActivityResult(requestCode, resultCode, data);
                //如果请求成功了
                if (resultCode == RESULT_OK)
                {
                        //请求码 为获取图片
                        switch (requestCode)
                        {
                                case LocalResourceIntent.requestCodeToGetImage:
                                        //选完图片后
                                        if (data != null)
                                        {
                                                //上传图片
                                                updateImage(data.getData());
                                        }
                                        break;

                        }
                }
        }


        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
        }

        @Override
        protected void onDestroy()
        {
                //请求删除未提交的图片
                deleteUnSubmitImage();

                super.onDestroy();
        }

        @Override
        public void onBackPressed()
        {
                showExitConfirmDialog();

        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item)
        {
                switch (item.getItemId())
                {
                        //如果点了返回键
                        case android.R.id.home:
                                //结束当前活动页
                                finish();
                                return true;
                }
                return super.onOptionsItemSelected(item);
        }


        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         */
        public static void startAction(Context context)
        {
                Intent intent = new Intent(context, PostSubmitActivity.class);
                context.startActivity(intent);
        }

        /**
         * 通过编辑页 启动本活动的静态方法
         * 提供文章id
         * static method to start current activity
         *
         * @param context
         * @param postId
         */
        public static void startAction(Context context, int postId)
        {
                Intent intent = new Intent(context, PostSubmitActivity.class);
                intent.putExtra(INTENT_POST_ID, postId);
                context.startActivity(intent);
        }


        public MediaDelegate getMediaDelegate()
        {
                return mediaDelegate;
        }

        public AlertDialog getProgressDialog()
        {
                return progressDialog;
        }
}
