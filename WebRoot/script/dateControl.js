function regDateControl(obj)
{
    //var oInput = document.getElementById(txtName);
    var oInput = obj;
    oInput.middleChar = "-";
    oInput.selectIndex = 1; 
    oInput.maxLength = 10;
    oInput.style.imeMode = "disabled";
    /*
	if(oInput.value ==""){
		oInput.value = specialText_GetDate(oInput.middleChar);
	}
	*/
	var splitArray;
	var str="";
	str=oInput.value;
	splitArray=str.split("-")
	if(splitArray.length!=3){
		oInput.value = specialText_GetDate(oInput.middleChar);
	}
	for(var i=0; i<splitArray.length; i++){
	  if( isNaN(splitArray[i]) ){
		  oInput.value = specialText_GetDate(oInput.middleChar);
	  }
	}
    oInput.charWidth = oInput.createTextRange().boundingWidth / oInput.maxLength;


    oInput.onclick = specialText_ClickEvent;
    oInput.onkeydown = specialText_KeyDownEvent;
    oInput.onfocus = function(){specialText_SelectYear(this);}
    oInput.onblur = function()
    {
        specialText_validYear(this);
        specialText_validMonth(this);
        specialText_validDate(this);
    }

    oInput.oncontextmenu = function(){return false;}
    oInput.ondrop = function(){return false;}
}


function specialText_ClickEvent()
{
    event.cancelBubble = true;
    specialText_validYear(this);
    specialText_validMonth(this);
    specialText_validDate(this);
    if(event.offsetX <= specialText_getCharWidth(this.charWidth,4))
        specialText_SelectYear(this);
    else if(event.offsetX > specialText_getCharWidth(this.charWidth,4)
            && event.offsetX <= specialText_getCharWidth(this.charWidth,7))
        specialText_SelectMonth(this);
    else if(event.offsetX > specialText_getCharWidth(this.charWidth,7))
        specialText_SelectDate(this);
}

function specialText_SelectYear(oInput)
{
    var oRange = oInput.createTextRange();
    oRange.moveStart("character",0);
    oRange.moveEnd("character",-6);

    oInput.selectIndex = 1;
    oRange.select();
}

function specialText_SelectMonth(oInput)
{
    var oRange = oInput.createTextRange();
    oRange.moveStart("character",5);
    oRange.moveEnd("character",-3);

    oInput.selectIndex = 2;
    oRange.select();
}

function specialText_SelectDate(oInput)
{
    var oRange = oInput.createTextRange();
    oRange.moveStart("character",8);

    oInput.selectIndex = 3;
    oRange.select();
}

function specialText_validYear(oInput)
{
    var arrValue = oInput.value.split(oInput.middleChar);
    var strYear = arrValue[0];

    if(parseInt(strYear,10) == 0)
        arrValue[0] = 2000;

    else if(strYear.length < 4)
        arrValue[0] = 2000 + parseInt(strYear,10);
    oInput.value = arrValue.join(oInput.middleChar);
}

function specialText_validMonth(oInput)
{
    var arrValue = oInput.value.split(oInput.middleChar);
    var strMonth = arrValue[1];

    if(parseInt(strMonth,10) == 0)
         arrValue[1] = "01";

    else if(strMonth.length < 2)
        arrValue[1] = "0" + strMonth;

    else if(parseInt(strMonth,10) > 12)
        arrValue[1] = "12";
    oInput.value = arrValue.join(oInput.middleChar);
}

function specialText_validDate(oInput)
{
    var arrValue = oInput.value.split(oInput.middleChar);
    var strYear = arrValue[0];
    var strMonth = arrValue[1];
    var strDate = arrValue[2];
    var intMonth = parseInt(strMonth,10);
    if(parseInt(strDate,10) == 0)
        arrValue[2] = "01";
    else if(strDate.length < 2)
        arrValue[2] = "0" + strDate;
    else
    {

        var monthMaxDates = specialText_getMonthDates(strYear,strMonth);
        if(parseInt(strDate,10) > monthMaxDates)
            arrValue[2] = monthMaxDates;
    }
    oInput.value = arrValue.join(oInput.middleChar);
}

function specialText_YearAdd(oInput,isMinus)
{
    var arrValue = oInput.value.split(oInput.middleChar);
    var strYear = arrValue[0];
    if(isMinus)
    {
        arrValue[0] = parseInt(strYear,10) - 1;
        if(parseInt(arrValue[0],10) < 1)
            arrValue[0] = "0001";
    }
    else
        arrValue[0] = parseInt(strYear,10) + 1;
    oInput.value = arrValue.join(oInput.middleChar);
    specialText_validYear(oInput);
    specialText_SelectYear(oInput);
}

function specialText_MonthAdd(oInput,isMinus)
{
    var arrValue = oInput.value.split(oInput.middleChar);
    var strMonth = arrValue[1];
    if(isMinus)
    {
        arrValue[1] = parseInt(strMonth,10) - 1;
        if(parseInt(arrValue[1],10) == 0)
            arrValue[1] = "12";
    }
    else
    {
        arrValue[1] = parseInt(strMonth,10) + 1;
        if(parseInt(arrValue[1],10) == 13)
            arrValue[1] = "01";
    }
    oInput.value = arrValue.join(oInput.middleChar);
    specialText_validMonth(oInput);
    specialText_SelectMonth(oInput);
}

function specialText_DateAdd(oInput,isMinus)
{
    var arrValue = oInput.value.split(oInput.middleChar);
    var strYear = arrValue[0];
    var strMonth = arrValue[1];
    var strDate = arrValue[2];
    var monthMaxDates = specialText_getMonthDates(strYear,strMonth);

    if(isMinus)
    {
        arrValue[2] = parseInt(strDate,10) - 1;
        if(parseInt(arrValue[2],10) == 0)
            arrValue[2] = monthMaxDates;
    }
    else
    {
        arrValue[2] = parseInt(strDate,10) + 1;
        if(parseInt(arrValue[2],10) == (monthMaxDates + 1))
            arrValue[2] = "01";
    }
    oInput.value = arrValue.join(oInput.middleChar);
    specialText_validDate(oInput);
    specialText_SelectDate(oInput);
}

function specialText_KeyDownEvent()
{

    if((event.keyCode >= 48 && event.keyCode <= 57) ||
       (event.keyCode >= 96 && event.keyCode <= 105))
    {     
        var oRange = document.selection.createRange();
        if(oRange.text.indexOf(this.middleChar) != -1)
            event.returnValue = false;
        else
            event.returnValue = true;
    }

    else if(event.keyCode >= 37 && event.keyCode <= 40)
    {
        event.returnValue = false;
        var keyCode = event.keyCode;

        if(keyCode == 37)
        {
            if(this.selectIndex == 1)
            {
                specialText_validYear(this);
                specialText_SelectDate(this);
            }
            else if(this.selectIndex == 2)
            {
                specialText_validMonth(this);
                specialText_SelectYear(this);
            }
            else if(this.selectIndex == 3)
            {
                specialText_validDate(this);
                specialText_SelectMonth(this);
            }
        }

        if(keyCode == 39)
        {
            if(this.selectIndex == 1)
            {
                specialText_validYear(this);
                specialText_SelectMonth(this);
            }
            else if(this.selectIndex == 2)
            {
                specialText_validMonth(this);
                specialText_SelectDate(this);
            }
            else if(this.selectIndex == 3)
            {
                specialText_validDate(this);
                specialText_SelectYear(this);
            }
        }


        if(keyCode == 38)
        {
            if(this.selectIndex == 1)
            {
                specialText_validYear(this);
                specialText_YearAdd(this,true);
            }
            else if(this.selectIndex == 2)
            {
                specialText_validMonth(this);
                specialText_MonthAdd(this,true);
            }
            else if(this.selectIndex == 3)
            {
                specialText_validDate(this);
                specialText_DateAdd(this,true);
            }
        }


        if(keyCode == 40)
        {
            if(this.selectIndex == 1)
            {
                specialText_validYear(this);
                specialText_YearAdd(this,false);
            }
            else if(this.selectIndex == 2)
            {
                specialText_validMonth(this);
                specialText_MonthAdd(this,false);
            }
            else if(this.selectIndex == 3)
            {
                specialText_validDate(this);
                specialText_DateAdd(this,false);
            }
        }
    }

    else if(event.keyCode == 116 || event.keyCode == 9)
        event.returnValue = true;
    else
    {
        event.returnValue = false;
        event.keyCode = 0;
    }
}



function specialText_GetDate(middleChar)
{
    var oDate = new Date();
    //alert(oDate);
    //alert(oDate.getMonth());
    return oDate.getYear() + middleChar
           + ((oDate.getMonth()+1) < 10 ? ("0" + (oDate.getMonth()+1)) : (oDate.getMonth()+1)) + middleChar
           + (oDate.getDate() < 10 ? ("0" + oDate.getDate()) : oDate.getDate());
    
}

function specialText_getCharWidth(charWidth,charNum)
{
    return charNum * charWidth;
}
function specialText_getMonthDates(strYear,strMonth)
{
    var intMonth = parseInt(strMonth,10);
    if(intMonth == 1 || intMonth == 3 || intMonth == 5 || intMonth == 7
       || intMonth == 8 || intMonth == 10 || intMonth == 12)
        return 31;
    else if(intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11)
        return 30;
    else
    {
        if(specialText_isLeapYear(strYear))
            return 29;
        else
            return 28;
    }
}
function specialText_isLeapYear(strYear)
{
    var intYear = parseInt(strYear,10);
    if((intYear % 4 == 0 && intYear % 100 != 0) ||
       (intYear % 100 == 0 && intYear % 400 == 0))
        return true;
    else
        return false;
}

/*
function init()
{
    regDateControl('date1');
}
*/