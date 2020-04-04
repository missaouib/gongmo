/**
 * Common JS
 * @author Jake
 * @date 20.04.04
 */
function multiDateRangePickerInit(options) {
	if (typeof(options) != "object") {
		return false;
	}

	var target;
	if (typeof(options.targetId) === "string") {
		target = $("#" + options.targetId);
	}
	else if (typeof(options.target) === "object") {
		target = options.target;
	} else {
		return false;
	}

	var st_dt;
	var en_dt;
	if (typeof(options.startName) === "string") {
		st_dt = $("input[name='" + options.startName + "']");
	} else if (typeof(options.startTarget) === "object") {
		st_dt = options.startTarget;
	} else {
		return false;
	}

	if (typeof(options.endName) === "string") {
		en_dt = $("input[name='" + options.endName + "']");
	} else if (typeof(options.endTarget) === "object") {
		en_dt = options.endTarget;
	} else {
		return false;
	}

	var yearCheck = false;
	var now = moment();
	if (typeof(options.year) === "string") {
		yearCheck = true;
		now = moment().set('year', options.year);
	}

	//Date range picker init
	target.daterangepicker({
		startDate: moment(),
		endDate: moment(),
		timePicker12Hour: false,
		format: 'YYYY-MM-DD',
		showDropdowns: true,
		showWeekNumbers: false,
		opens: 'right',
		locale: {
			applyLabel: '저장',
			cancelLabel: '초기화',
			fromLabel: 'From',
			toLabel: 'To',
			daysOfWeek: ['일', '월', '화', '수', '목', '금', '토'],
			monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
			firstDay: 1
		},
		autoUpdateInput: true
	});

	/*
	var setShowDateRange = function () {
		var startDate = target.data('daterangepicker').startDate.format('DD/MM/YYYY');
		var endDate = target.data('daterangepicker').endDate.format('DD/MM/YYYY');
		var startDateTime = target.data('daterangepicker').startDate.format('YYYY-MM-DDT00:00:00');
		var endDateTime = target.data('daterangepicker').endDate.format('YYYY-MM-DDT23:59:59');
		target.val(startDate + " ~ " + endDate);
		st_dt.val(startDateTime);
		en_dt.val(endDateTime);
	};
	*/

	var setShowDateRange = function () {
		var startDate = target.data('daterangepicker').startDate.format('YYYY-MM-DD');
		var endDate = target.data('daterangepicker').endDate.format('YYYY-MM-DD');
		var startDateTime = target.data('daterangepicker').startDate.format('YYYY-MM-DDT00:00:00');
		var endDateTime = target.data('daterangepicker').endDate.format('YYYY-MM-DDT23:59:59');
		target.val(startDate + " ~ " + endDate);
		st_dt.val(startDateTime);
		en_dt.val(endDateTime);
	};

	//init search date start / end
	if (st_dt.val().length > 0 && en_dt.val().length > 0) {
		/*
		if (yearCheck) {
			target.data("daterangepicker").setStartDate(moment(st_dt.val(), "DD/MM/YYYY").set('year', options.year));
			target.data("daterangepicker").setEndDate(moment(en_dt.val(), "DD/MM/YYYY").set('year', options.year));
		} else {
			target.data("daterangepicker").setStartDate(moment(st_dt.val(), "DD/MM/YYYY"));
			target.data("daterangepicker").setEndDate(moment(en_dt.val(), "DD/MM/YYYY"));
		}
		*/

		target.data("daterangepicker").setStartDate(moment(st_dt.val(), "YYYY-MM-DD"));
		target.data("daterangepicker").setEndDate(moment(en_dt.val(), "YYYY-MM-DD"));
		setShowDateRange();
	} else {
		target.val('');
	}

	//date range picker apply event
	target.on('apply.daterangepicker', function (e, picker) {
		setShowDateRange();
	});

	//date range picker cancel event
	target.on('cancel.daterangepicker', function (e, picker) {
		$(this).val('');
		st_dt.val('');
		en_dt.val('');
	});

	target.on('hide.daterangepicker', function (e, picker) {
		setShowDateRange();
	});
};

var iCheckInit = function() {
	$('input[type="checkbox"], input[type="radio"]').iCheck({
		checkboxClass: 'icheckbox_square-blue',
		radioClass: 'iradio_flat-blue'
	});
};

function pagination(page) {
	var $frmSearch = $("form[name='frmSearch']");
	$frmSearch.find("input[name='page']").val(page);
	$frmSearch.submit();
}

function drawAttachmentStore($params) {


}

function drawAttachments($params) {

	if ($params === null || $params === undefined) return false;

	var data = $params.rs_data;

	var originalFileName = data.orgFilename,
        s3FileName = data.savedFilename,
        fullPath = data.fullPath,
        fileType = data.fileType,
		thumbnailPath = data.thumbnailPath;

	var downloadUri = '/file/download?orgFilename=' + originalFileName + '&fullPath=' + fullPath;

    var tags = "";
    if (fileType === 'Image' || 'Thumbnail') {
        tags = "<a href='" + downloadUri + "'> <img src='" + thumbnailPath + "'></a>";
    } else {
        tags = "<a href='" + downloadUri + "'>" + originalFileName + "</a>";
    }

	return tags;
}

var onlyNumberKeyEvent = function (options) {
	if (typeof(options) !== 'object') return false;

	var option = {};
	option.className = "only-number";
	option.formId = "";

	$.extend(options, option);

	var target = "";
	if (option.formId === "") {
		target = $("." + option.className);
	} else {
		target = $("." + option.className, $("#" + option.formId));
	}

	target.each(function() {
		$(this).unbind("keydown").keydown(function (e) {
			// Allow: backspace, delete, tab, escape, enter, and, -, .
			if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 189, 190]) !== -1 ||
				// Allow: Ctrl+A
				(e.keyCode == 65 && e.ctrlKey === true) ||
				// Allow: home, end, left, right
				(e.keyCode >= 35 && e.keyCode <= 39)) {
				return ;
			}
			// Ensure that it is a number and stop the keypress
			if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
				e.preventDefault();
			}
		});
	});

	target.keyup(function(e) {
		var inputValue = $(this).val();
		if(e.keyCode == 8) return;
		$(this).val(onlyNumber(inputValue));
	});
};

var onlyNumber = function(str) {
	var len = str.length;
	var result = "";

	for (var i=0; i<len; i++) {
		if ((str.charAt(i) >= "0") && (str.charAt(i) <= "9") || str.charAt(i) === "." || str.charAt(i) === "-") {
			result += str.charAt(i);
		}
	}
	return result;
};

var searchCheck = function(searchCodeObj, searchDto) {
	if (searchDto === null || searchDto === "") return;

	var resetYn = searchDto.reset;

	if(resetYn === 'Y') { return false; }

	for(var i=0; i<searchCodeObj.length; i++){
		if (searchCodeObj[i].searchCode != null) {

			var $chkCode = $(":checkbox[name=" + searchCodeObj[i].codeName + "]");
			$(searchCodeObj[i].allCheckId).iCheck('uncheck');
			if ($chkCode.length === searchCodeObj[i].searchCode.length) {
				$(searchCodeObj[i].allCheckId).iCheck('check');
			}

			$chkCode.each(function (idx, chk) {
				var $checkbox = $(chk);
				$(searchCodeObj[i].searchCode).each(function(idx, contentType) {
					if ($checkbox.val() === contentType) {
						$checkbox.iCheck('check');
					}
				});
			});
		}
	}
};

var thumbnailUpload = function () {
	var $this = $(this);
	var $imagePanel = $this.closest('.image-panel');

	documentUpload({
		multiple: false,
		accept: '.jpg, .png',
		position: 1,
		callback: function (response) {

			// init
			$imagePanel.find('.thumbnailInfo').remove();

			var tag = "<div class=\"thumbnailInfo\">";
			tag += "<img src='" + response.rs_data.thumbnailPath + "'>";
			tag += "</div>";

			$imagePanel.append(tag);
			$imagePanel.find('.thumbnailInfo').data('thumbnail-data', response.rs_data);
			$('.update-image, .remove-image').removeClass('hide');
			$('#thumbnailUpload').addClass('hide');
		}
	})
};

var thumbnailRemove = function () {
	var $this = $(this);
	var $imagePenel = $this.closest(".image-panel");

	commonModal({
		contents: "파일을 삭제하시겠습니까?",
		submit: function () {
			$imagePenel.find('.thumbnailInfo').remove();
			$imagePenel.find('img').remove();
			$('.update-image, .remove-image').addClass('hide');
			$('#thumbnailUpload').removeClass('hide');
		}
	});
};

var thumbnailLoad = function (dto) {
	if (dto === null || dto === "") return;

	if (dto.attachments.length > 0) {
		$.each(dto.attachments, function (idx, item) {
			if(item.position == 1) {
				var $imagePanel = $(".image-panel");
				// 썸네일
				var tag = "<div class=\"thumbnailInfo\">";
				tag += "<a href='/file/download?id=" + item.id + "'><img src='" + item.thumbnailPath + "'></a>";
				tag += "</div>";

				$imagePanel.append(tag);
				$imagePanel.find(".thumbnailInfo").last().data("thumbnail-data", item);
				$('.update-image, .remove-image').removeClass('hide');
				$('#thumbnailUpload').addClass('hide');
			} else {
				if (item.fullUri === "null" || item.fullUri === null) return ;
				var $fileWrapper = $(".attachment");
				var $filePanel = $fileWrapper.find('.filePanel');

				var tag = "<li style=\"position: relative;\" class=\"fileInfo\">";
				tag += "<button type=\"button\" class=\"btn btn-default btn-sm pull-right btnRemoveFile\" style=\"position: absolute; top: 5px; right: 5px;\"><i class=\"fa fa-trash-o\"></i></button>";
				tag += "<span class=\"mailbox-attachment-icon\"><i class=\"fa fa-file-text-o\"></i></span>";
				tag += "<div class=\"mailbox-attachment-info\">";
				tag += "<a href='/file/download?id=" + item.id  + "' class='mailbox-attachment-name'>";
				tag += "<i class=\"fa fa-paperclip\"></i> "+ item.orgFilename;
				tag += "</a>";
				tag += "</div>";
				tag += "</li>";

				$filePanel.append(tag);
				$filePanel.find(".fileInfo").last().data("file_data", item);
			}
		})
	}
};

