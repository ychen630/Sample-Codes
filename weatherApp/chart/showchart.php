
<?php
	date_default_timezone_set('America/Los_Angeles');
	if(isset($_POST["input1"])){
		//KoolChart Library
		$KoolControlsFolder = "KoolPHPSuite/KoolControls";//Relative path to "KoolPHPSuite/KoolControls" folder	 
		require $KoolControlsFolder."/KoolChart/koolchart.php";
	 
		$chart = new KoolChart("chart");
		$chart->scriptFolder=$KoolControlsFolder."/KoolChart";	
		$chart->Width = 750;
		$chart->Height = 480;
	 
		$chart->Title->Text = "Forecast Temperature for 5 Days"; //chart title
		
		//x-axis
		$chart->PlotArea->XAxis->Title = "Date"; 
		$day1 = date("m/d/Y", strtotime("+1 day"));
		$day2 = date("m/d/Y", strtotime("+2 day"));
		$day3 = date("m/d/Y", strtotime("+3 day"));
		$day4 = date("m/d/Y", strtotime("+4 day"));
		$day5 = date("m/d/Y", strtotime("+5 day"));
		$chart->PlotArea->XAxis->Set(array($day1,$day2,$day3,$day4,$day5));
		
		//y-axis
		$chart->PlotArea->YAxis->Title = "Temperature °C";
		$chart->PlotArea->YAxis->MaxValue = max($_POST["input1"],$_POST["input2"],$_POST["input3"],$_POST["input4"],$_POST["input5"]) + 5;
		$chart->PlotArea->YAxis->MinValue = min($_POST["input1"],$_POST["input2"],$_POST["input3"],$_POST["input4"],$_POST["input5"]) - 5;
		$chart->PlotArea->YAxis->MajorStep = 5;
		$chart->PlotArea->YAxis->MinorStep = 1;
		$chart->PlotArea->YAxis->LabelsAppearance->DataFormatString = "{0}";
		
		//line chart
		$series = new LineSeries();
		$series->Name = $_POST["input6"].", ".$_POST["input7"];
		$series->Appearance->BackgroundColor="#2D6B99";
		$series->TooltipsAppearance->DataFormatString = "{0}% {1}";
		$series->LabelsAppearance->Position = "Above";
//		$series->MarkersAppearance->MarkersType = "Square";
		//temperature value
		$series->ArrayData(array($_POST["input1"],$_POST["input2"],$_POST["input3"],$_POST["input4"],$_POST["input5"]));
		$chart->PlotArea->AddSeries($series);
	}
 
?>


<form id="form1" method="post">
	<?php if(isset($_POST["input1"])){echo $chart->Render();}?>					
</form>
