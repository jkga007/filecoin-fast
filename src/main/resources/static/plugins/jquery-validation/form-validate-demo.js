
        var layerTipsMap = new Map();

        //以下为修改jQuery Validation插件兼容Bootstrap的方法，没有直接写在插件中是为了便于插件升级
        $.validator.setDefaults({
            highlight: function (element) {
                // $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                // $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
            },
            success: function (element) {
                // var remErrorId = element.attr("id").substring(0,element.attr("id").length -6);
                // var tipId = layerTipsMap.get(""+remErrorId);
                // if(typeof(tipId) != "undefined"){
                //     Core.close(tipId);
                // }
                // element.closest('.form-group').removeClass('has-error').addClass('has-success');
            },
            // errorElement: "span",
            //错误提示，错误对象
            errorPlacement:function(error,element){

                var nowId = element.attr("id")+"";

                var replaceIdObj = $("#"+nowId+"_changeTipsHere");
                var replaceId = replaceIdObj.attr("id");
                if(typeof replaceId != "undefined"){
                    element = replaceIdObj;
                }
                //去掉该节点的错误信息提示
                var tipId = layerTipsMap.get(""+element.attr("id"));
                if(typeof(tipId) != "undefined"){
                    Core.close(tipId);
                }
                if(error[0].innerText){
                    // 提示新的错误信息
                    // 1.错误信息，2提示位置，3同时提示多个错误
                    var layerTipIndex = layer.tips(error[0].innerText,element,{
                        //错误信息可以同时提示多个，...
                        tipsMore:true,
                        time: 100000
                    });
                    //放入对应的element的错误信息
                    layerTipsMap.put(""+element.attr("id"),layerTipIndex);
                }
                // if (element.is(":radio") || element.is(":checkbox")) {
                //     error.appendTo(element.parent().parent().parent());
                // } else {
                //     error.appendTo(element.parent());
                // }
                // error.hide();

            },
            // errorClass: "help-block m-b-none",
            // validClass: "help-block m-b-none",
            onfocusout:function(element){
                $(element).valid();
            },
            onkeyup:function(element){
                $(element).valid();
            }

        });

        //以下为官方示例
        $().ready(function () {
            // validate the comment form when it is submitted
            // $("#commentForm").validate();

            // validate signup form on keyup and submit
           

            // propose username by combining first- and lastname
            $("#username").focus(function () {
                var firstname = $("#firstname").val();
                var lastname = $("#lastname").val();
                if (firstname && lastname && !this.value) {
                    this.value = firstname + "." + lastname;
                }
            });
        });
