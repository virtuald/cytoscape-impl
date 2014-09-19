package org.cytoscape.welcome.internal.panel;

/*
 * #%L
 * Cytoscape Welcome Screen Impl (welcome-impl)
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2006 - 2013 The Cytoscape Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import org.apache.poi.ss.usermodel.*;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.io.read.CyNetworkReaderManager;
import org.cytoscape.io.read.CyTableReader;
import org.cytoscape.io.read.CyTableReaderManager;
import org.cytoscape.io.webservice.WebServiceClient;
import org.cytoscape.model.*;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.NetworkTaskFactory;
import org.cytoscape.task.read.LoadTableFileTaskFactory;
import org.cytoscape.util.swing.FileChooserFilter;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.work.*;
import org.cytoscape.work.swing.DialogTaskManager;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.*;
import java.util.List;

public class GeneSearchPanel extends AbstractWelcomeScreenChildPanel implements ActionListener
{
	private JComboBox species = new JComboBox();
	private JTextArea geneList = new JTextArea();
	private JTextField dataFileField = new JTextField("Optional");
	private JButton setDataFileButton = new JButton("Browse...");
	private JButton buildNetworkButton = new JButton("Import Network");
	private File dataTableFile = null;


	private final DialogTaskManager taskManager;
	private final CyNetworkReaderManager networkReaderManager;
	private final CyNetworkManager networkManager;
	private final CyNetworkViewFactory networkViewFactory;
	private final CyLayoutAlgorithmManager layoutAlgorithmManager;
	private final VisualMappingManager visualMappingManager;
	private final CyNetworkViewManager networkViewManager;
	private final WebServiceClient webServiceClient;
	private final FileUtil fileUtil;
	private final CySwingApplication cytoscapeDesktop;
	private final LoadTableFileTaskFactory loadTableFileTaskFactory;
	private	final NetworkTaskFactory edgeBundlerTaskFactory;
	private final CyServiceRegistrar serviceRegistrar;
	private final CyTableReaderManager cyTableReaderManager;


	public GeneSearchPanel(final DialogTaskManager taskManager, CyNetworkReaderManager networkReaderManager, CyNetworkManager networkManager, CyNetworkViewFactory networkViewFactory, CyLayoutAlgorithmManager layoutAlgorithmManager, VisualMappingManager visualMappingManager, CyNetworkViewManager networkViewManager, WebServiceClient webServiceClient, FileUtil fileUtil, CySwingApplication cytoscapeDesktop, LoadTableFileTaskFactory loadTableFileTaskFactory, NetworkTaskFactory edgeBundlerTaskFactory, CyServiceRegistrar serviceRegistrar, CyTableReaderManager cyTableReaderManager)
	{
		this.taskManager = taskManager;
		this.networkReaderManager = networkReaderManager;
		this.networkManager = networkManager;
		this.networkViewFactory = networkViewFactory;
		this.layoutAlgorithmManager = layoutAlgorithmManager;
		this.visualMappingManager = visualMappingManager;
		this.networkViewManager = networkViewManager;
		this.webServiceClient = webServiceClient;
		this.fileUtil = fileUtil;
		this.cytoscapeDesktop = cytoscapeDesktop;
		this.loadTableFileTaskFactory = loadTableFileTaskFactory;
		this.edgeBundlerTaskFactory = edgeBundlerTaskFactory;
		this.serviceRegistrar = serviceRegistrar;
		this.cyTableReaderManager = cyTableReaderManager;
		initComponents();
	}

	private void initComponents()
	{
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 5));
		species.setOpaque(true);
		species.setBackground(PANEL_COLOR);
		buildNetworkButton.setOpaque(true);
		buildNetworkButton.setBackground(PANEL_COLOR);
		
		JLabel speciesLabel = new JLabel( "Species" );
		speciesLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		species.setAlignmentX( Component.LEFT_ALIGNMENT );
		
		JLabel genesLabel = new JLabel( "Genes" );
		genesLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
		geneList.setPreferredSize( new Dimension(200,200) );
		
		JScrollPane sp = new JScrollPane( geneList );
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setAlignmentX(Component.LEFT_ALIGNMENT);
		sp.setPreferredSize(new Dimension(sp.getPreferredSize().width, 320));
		
		species.addItem("human");
		species.addItem("mouse");
		species.addItem("yeast");
		species.addItem("ecoli");
		species.addItem("rat");
		species.addItem("measew");
		species.addItem("caeel");
		species.addItem("trepa");
		species.addItem("i34a1");
		species.addItem("xenla");
		species.addItem("drome");
		species.addItem("arath");
		species.addItem("bacsu");
		species.addItem("hcvco");
		species.addItem("hrsva");
		species.addItem("camje");
		species.addItem("ebvb9");
		species.addItem("hhv11");
		species.addItem("syny3");
		species.addItem("hv1h2");
		species.addItem("9hiv1");
		species.addItem("chick");
		species.addItem("sv40");
		species.addItem("hcvh");
		species.addItem("hpv16");
		species.addItem("bovin");
		species.addItem("theko");
		species.addItem("canen");
		species.addItem("i97a1");
		species.addItem("danre");

		JLabel dataLabel = new JLabel("Merge Gene Attribute Table (Excel)");
		dataFileField.setEnabled(false);

		final GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateContainerGaps(false);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING, true)
						.addComponent(speciesLabel)
						.addComponent(species, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(genesLabel)

						.addComponent(dataLabel)
						.addGroup(layout.createSequentialGroup()
										.addComponent(dataFileField)
										.addComponent(setDataFileButton)
						)
						.addGroup(layout.createParallelGroup(Alignment.TRAILING, true)
										.addComponent(sp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(buildNetworkButton)
						)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
						.addComponent(speciesLabel)
						.addComponent(species)
						.addComponent(genesLabel)
						.addComponent(sp)
						.addComponent(dataLabel)
						.addGroup(layout.createParallelGroup()
										.addComponent(dataFileField)
										.addComponent(setDataFileButton)
						)
						.addComponent(buildNetworkButton)
		);
		
		addActionListeners();

	}

	private void addActionListeners()
	{
		buildNetworkButton.addActionListener(this);

		setDataFileButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Setup a the file filter for the open file dialog
				FileChooserFilter fileChooserFilter = new FileChooserFilter("Data Table File",
						new String[]{""});

				Collection<FileChooserFilter> fileChooserFilters = new LinkedList<FileChooserFilter>();
				fileChooserFilters.add(fileChooserFilter);

				// Show the dialog
				dataTableFile = fileUtil.getFile(cytoscapeDesktop.getJFrame(),
						"Choose Data Table", FileUtil.LOAD, FileUtil.LAST_DIRECTORY, "Set", fileChooserFilters);

				dataFileField.setText(dataTableFile.getAbsolutePath());
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( geneList.getText().trim().isEmpty() )
			return;

		String selectedSpecies = species.getSelectedItem().toString();
		java.util.List<String> geneNames = Arrays.asList(geneList.getText().split("\\s+"));


		String query = "species:" + selectedSpecies;
		if( geneNames != null && !geneNames.isEmpty() )
		{
			query += " AND ";
			if( geneNames.size() > 1 )
				query += "( ";
			for( int i = 0; i < geneNames.size() - 1; i++ )
			{
				String geneName = geneNames.get(i);
				query += "alias:" + geneName + " OR ";
			}
			query += "alias:" + geneNames.get(geneNames.size()-1);
			if( geneNames.size() > 1 )
				query += " )";
		}

		closeParentWindow();
		TaskIterator ti = webServiceClient.createTaskIterator(query);
		taskManager.execute( ti, new TaskObserver()
		{
			CyNetwork network;

			@Override
			public void taskFinished(ObservableTask task)
			{
				Object networks = task.getResults(Object.class);
				if( networks instanceof Set )
				{
					Set networkSet = (Set)networks;
					for( Object o : networkSet )
					{
						if( o instanceof CyNetwork )
							network = (CyNetwork)o;
						return;
					}
				}

			}

			@Override
			public void allFinished(FinishStatus finishStatus)
			{
				Collection<CyNetworkView> views = networkViewManager.getNetworkViews(network);
				CyLayoutAlgorithm layoutAlgorithm = layoutAlgorithmManager.getLayout("force-directed");
				for( final CyNetworkView view : views )
				{
					Object ctx = layoutAlgorithm.getDefaultLayoutContext();
					TaskIterator ti = layoutAlgorithm.createTaskIterator(view, ctx, CyLayoutAlgorithm.ALL_NODE_VIEWS, "");

					Task styleTask = new AbstractTask()
					{
						@Override
						public void run(TaskMonitor taskMonitor) throws Exception
						{
							VisualStyle defaultStyle = visualMappingManager.getDefaultVisualStyle();
							visualMappingManager.setVisualStyle(defaultStyle, view);
							defaultStyle.apply(view);
					 		view.updateView();
						}
					};
					ti.append(styleTask);

					TaskIterator edgeBundleTaskIterator = edgeBundlerTaskFactory.createTaskIterator(view.getModel());
					//Task edgeBundleTask = edgeBundleTaskIterator.next();
					Map<String, Object> edgeBundlingTaskSettings = new HashMap<String, Object>();
					edgeBundlingTaskSettings.put("numNubs", 3);
					edgeBundlingTaskSettings.put("K", 0.003);
					edgeBundlingTaskSettings.put("COMPATABILITY_THRESHOLD", 0.3);
					edgeBundlingTaskSettings.put("maxIterations", 5000);
					TunableSetter setter = serviceRegistrar.getService(TunableSetter.class);
					ti.append( setter.createTaskIterator(edgeBundleTaskIterator, edgeBundlingTaskSettings) );

					if( dataTableFile != null )
					{
						class MergeTableTask extends AbstractTask
						{
							private final int UNKNOWN = 100;
							private final int DOUBLE = 101;
							private final int INTEGER = 102;
							private final int MIXED = 103;

							private int getColumnType(Sheet sheet, int columnIndex)
							{
								if( columnIndex == 7 )
								{
									int x = 5;
								}
								int currentResult = UNKNOWN;
								boolean preferDouble = false;
								for( int i = 1; i <= sheet.getLastRowNum(); i++ )
								{
									Row row = sheet.getRow(i);
									if( row == null )
										continue;
									Cell cell = row.getCell(columnIndex);
									if( cell == null )
										continue;
									int type = getCellType(cell, preferDouble);
									if( currentResult == UNKNOWN )
									{
										currentResult = type;
										if( type == DOUBLE )
											preferDouble = true;
									}
									else if( currentResult == INTEGER && type == DOUBLE )
									{
										currentResult = DOUBLE;
										preferDouble = true;
									}
									else if( currentResult != type )
										return MIXED;
								}
								return currentResult;
							}


							private int getCellType(Cell cell, boolean preferDouble)
							{
								int type = cell.getCellType();
								if( type == Cell.CELL_TYPE_NUMERIC )
								{
									if( preferDouble )
										return DOUBLE;
									String number = convertDoubleToString(cell.getNumericCellValue());
									try
									{
										Integer.parseInt(number);
										type = INTEGER;
									}
									catch (NumberFormatException e)
									{
										type = DOUBLE;
									}
								}
								return type;
							}

							private String convertDoubleToString(Double v)
							{
								BigDecimal bd = new BigDecimal(v);
								try
								{
									BigInteger bi = bd.toBigIntegerExact();
									return bi.toString();
								}
								catch( ArithmeticException e )
								{
									return v.toString();
								}
							}



							@Override
							public void run(TaskMonitor taskMonitor) throws Exception
							{
								final Workbook workbook = WorkbookFactory.create( new FileInputStream(dataTableFile) );
								final Sheet sheet = workbook.getSheetAt(0);

								CyNetwork network  = view.getModel();
								CyTable table = network.getDefaultNodeTable();

								//Create columns and also save their names in a list for later.
								class ColumnInfo
								{
									int columnIndex;
									String columnName;
									Class columnType;

									ColumnInfo(int columnIndex, String columnName, Class columnType)
									{
										this.columnIndex = columnIndex;
										this.columnName = columnName;
										this.columnType = columnType;
									}
								}

								List<ColumnInfo> addedColumns = new ArrayList<ColumnInfo>();
								Row firstRow = sheet.getRow(0);
								for(int i = 1; i < firstRow.getLastCellNum(); i++)
								{
									String columnName = firstRow.getCell(i).toString();
									if( columnName.trim().isEmpty() )
										continue;
									int type = getColumnType(sheet, i);
									if( type == UNKNOWN )
										continue;
									Class typeClass = null;
									if( type == Cell.CELL_TYPE_STRING || type == MIXED )
										typeClass = String.class;
									else if( type == INTEGER )
										typeClass = Integer.class;
									else if( type == DOUBLE )
										typeClass = Double.class;
									else if( type == Cell.CELL_TYPE_BOOLEAN )
										typeClass = Boolean.class;
									else
										//If it isn't one of the above, use String...
										typeClass = String.class;


									if( table.getColumn(columnName) == null )
									{
										table.createColumn(columnName, typeClass, false);
										addedColumns.add( new ColumnInfo(i, columnName, typeClass));
									}
								}

								//Create a set which contains all of the rows to be added.
								//The "name" of a row is the contents of the first cell in the row.
								HashMap<String,Integer> rowNames = new HashMap<String,Integer>();
								for( int i = 1; i <= sheet.getLastRowNum(); i++ )
								{
									Row row = sheet.getRow(i);
									if( row == null )
										continue;
									Cell firstCell = row.getCell(0);
									if( firstCell == null )
										continue;
									rowNames.put(firstCell.toString(), i);
								}

								for( CyNode node : network.getNodeList() )
								{
									CyRow cyRow = network.getRow(node);
									String cyRowName = cyRow.get(CyNetwork.NAME, String.class);
									if( rowNames.keySet().contains(cyRowName) )
									{
										int r = rowNames.get(cyRowName);
										for( int i = 0; i < addedColumns.size(); i++ )
										{
											ColumnInfo columnInfo = addedColumns.get(i);
											int c = columnInfo.columnIndex;
											Cell cell = sheet.getRow(r).getCell(c);
											Object data = null;
											try
											{
												if (columnInfo.columnType == String.class)
													data = cell.toString();
												else if (columnInfo.columnType == Integer.class)
												{
													BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
													try
													{
														BigInteger bi = bd.toBigIntegerExact();
														data = bi.intValue();
													}
													catch( ArithmeticException e)
													{
														data = null;
													}

												}
												else if (columnInfo.columnType == Double.class)
													data = cell.getNumericCellValue();
												else if (columnInfo.columnType == Boolean.class)
													data = cell.getBooleanCellValue();
											}
											catch (IllegalStateException e)
											{
												data = null;
											}
											if( data != null )
												cyRow.set(columnInfo.columnName, data);
										}
									}
								}
							}

						}


						MergeTableTask mergeTableTask = new MergeTableTask();

						ti.append(mergeTableTask);

					}








//					Map<String,Object> loadTableFileTaskSettings = new HashMap<String, Object>();
//
//					//whereImportTable
//					String selectedImportLocation = "To a Network Collection";
//					ListSingleSelection<String> whereImportTable = new ListSingleSelection<String>(selectedImportLocation);
//					whereImportTable.setSelectedValue(selectedImportLocation);
//					loadTableFileTaskSettings.put("whereImportTable", whereImportTable);
//
//					//targetNetworkCollection
//					//The collection name is the same as the network name.
//					CyNetwork network = view.getModel();
//					String selectedCollection = network.getRow(network).get(CyNetwork.NAME, String.class);
//					ListSingleSelection<String> targetNetworkCollection = new ListSingleSelection<String>(selectedCollection);
//					whereImportTable.setSelectedValue(selectedCollection);
//					loadTableFileTaskSettings.put("targetNetworkCollection", targetNetworkCollection);
//
//					//keyColumnForMapping
//					String selectedColumnName = "shared name";
//					ListSingleSelection<String> keyColumnForMapping = new ListSingleSelection<String>(selectedColumnName);
//					whereImportTable.setSelectedValue(selectedColumnName);
//					loadTableFileTaskSettings.put("keyColumnForMapping", keyColumnForMapping);
//
//					//dataTypeTargetForNetworkList
//					String selectedImportDataType = "Node Table Columns";
//					ListSingleSelection<String> dataTypeTargetForNetworkList = new ListSingleSelection<String>(selectedImportDataType);
//					whereImportTable.setSelectedValue(selectedImportDataType);
//					loadTableFileTaskSettings.put("dataTypeTargetForNetworkList", dataTypeTargetForNetworkList);
//
//					TaskIterator loadTableFileTaskIterator = loadTableFileTaskFactory.createTaskIterator(dataTableFile);
//					TaskIterator tunablesSetLoadTableFileTaskIterator = setter.createTaskIterator(loadTableFileTaskIterator, loadTableFileTaskSettings);
//
//					if( dataTableFile != null )
//					{
//						ti.append(loadTableFileTaskIterator);
//					}
					taskManager.execute( ti );

				}
			}
		});
	}
}
