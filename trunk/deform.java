package de4m;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import de4m.*;

class deform extends JFrame implements ActionListener {
	
db db = new db();
int sdd;

public static void main(String args[]) {
	try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {}
	deform gui = new deform();
	gui.setVisible(true);}

public deform() {
	// ESTABLISH
	buildgroups();
	PC.addTab("<HTML><B>CONNECTION", PB);
	PC.addTab("<HTML><B><FONT COLOR=\"#666666\">GENERAL / WEAPONS / ARMOR", MASTER1);
	PC.addTab("<HTML><B><FONT COLOR=\"#666666\">MISC / RACE+CLASS / STATS", MASTER2);
	wepdmg.addTab("Primary dmg", wepdmg1);
	wepdmg.addTab("Secondary dmg", wepdmg2);
	PC.setEnabledAt(1, false);
	PC.setEnabledAt(2, false);
	PC.setToolTipTextAt(1, "You must import an item before editing.");
	PC.setToolTipTextAt(2, "You must import an item before editing.");
	PB3.add(IMPORTlabel);
	PB3.add(IMPORTTHIS);
	PB3.add(IMPORTbuttonA);
	PB3.add(CREATElabel);
	PB3.add(NEWENTRYFIELD);
	PB3.add(CREATEbuttonA);
	PB4.add(HELPbutton);
	PB4.add(CONNECTbutton);
	INOUTBOX.add(INOUT);
	// STYLE
	Border sp = BorderFactory.createMatteBorder(0, 4, 0, 4, Color.gray);
	PI.setBorder(sp);
	PQ.setBorder(sp);
	PB.setLayout(new BoxLayout(PB, BoxLayout.Y_AXIS));
	PB2.setLayout(new FlowLayout(FlowLayout.LEFT));
	PB3.setLayout(new FlowLayout(FlowLayout.LEFT));
	PB4.setLayout(new FlowLayout(FlowLayout.RIGHT));
	MASTER1.add(PH, BorderLayout.WEST);
	MASTER1.add(PI, BorderLayout.CENTER);
	MASTER1.add(PJ, BorderLayout.EAST);
	MASTER2.add(PK, BorderLayout.WEST);
	MASTER2.add(PQ, BorderLayout.CENTER);
	MASTER2.add(PS, BorderLayout.EAST);
	INOUTBOX.setLayout(new FlowLayout(FlowLayout.LEFT));
	INOUT.setLineWrap(true);
	INOUT.setWrapStyleWord(true);
	INOUT.setEditable(false);
	INOUT.setOpaque(false);
	INOUT.setFont(new Font("Arial", Font.PLAIN, 12));
	NULLCB.setSelected(true);
	// FINALIZE
	add(PC);
	pack();
	setLocationRelativeTo(null);
	setTitle("Deform alpha.0.7");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// LISTENERS
	R_allCB.addActionListener(this);
	C_allCB.addActionListener(this);
	CONNECTbutton.addActionListener(this);
	IMPORTbuttonA.addActionListener(this);
	IMPORTTHIS.addActionListener(this);
	importSEL.addActionListener(this);
	HELPbutton.addActionListener(this);
	CREATEbuttonA.addActionListener(this);}

public void actionPerformed(ActionEvent e) {
	if (e.getSource() == R_allCB) {
		for (int i = 0 ; i < 10 ; i++){CBarray[i].setSelected(R_allCB.isSelected());}}
	if (e.getSource() == C_allCB) {
		for (int i = 10 ; i < 20 ; i++){CBarray[i].setSelected(C_allCB.isSelected());}}
	if (e.getSource() == HELPbutton) {showhelp();}
	if (e.getSource() == CONNECTbutton) {establishConnection();}
	if (e.getSource() == IMPORTbuttonA) {executeLookup();}
	if (e.getSource() == IMPORTTHIS) {executeLookup();}
	if (e.getSource() == importSEL) {executeImport();}
	if (e.getSource() == CREATEbuttonA) {executeExport();}}

public void showhelp() {
	INOUT.setText(helptext);}

public void establishConnection() {
	String passs = "";
	for (char next : MYSQLpass.getPassword()) {
		passs += next;}
	db.dbConnect(MYSQLhost.getText(), MYSQLdb.getText(), MYSQLuser.getText(), passs);
	INOUT.setText(db.getstatus());
	CONNECTbutton.setText("<HTML><B><FONT COLOR=\"#" + db.getcolor() + "\">##</FONT> Connect <FONT COLOR=\"#" + db.getcolor() + "\">##</FONT>");}

public void executeLookup() {
	PB2.remove(IMPORTDD);
	try{IMPORTDD = new JComboBox(db.dbLookup(IMPORTTHIS.getText()));
		PB2.add(IMPORTDD);
		PB2.add(importSEL);
		PB2.revalidate();
		PB2.repaint();
		pack();
	} catch (Exception e) {
		INOUT.setText("Could not execute lookup.\n-Have you connected to MySQL database yet?\n-Does search string include ' or \" ?");}}

public void endlookup(){
	PB2.remove(IMPORTDD);
	PB2.remove(importSEL);
	PB2.revalidate();
	PB2.repaint();
	pack();}

public void opentabs(){
	PC.setEnabledAt(1, true);
	PC.setEnabledAt(2, true);
	PC.setToolTipTextAt(1, null);
	PC.setToolTipTextAt(2, null);
	PC.setSelectedIndex(1);
	PC.setTitleAt(1, "<HTML><B>GENERAL / WEAPONS / ARMOR");
	PC.setTitleAt(2, "<HTML><B>MISC / RACE+CLASS / STATS");}

  ////////////
 // IMPORT //
////////////

public void executeImport() {
	try{
	db.setimportentry(IMPORTDD.getSelectedIndex());
	endlookup();
	for (JCheckBox next : CBarray) { // turn off all checkboxes
		next.setSelected(false);}
	
	for (int i = 0 ; i < edits.length - 15 ; i++){ // basic fields
		if (edits[i] instanceof JTextField){
			((JTextField)edits[i]).setText(db.dbImport(fields[i]));}
		else{if (db.dbInt(fields[i])-mods[i]>=0){
			((JComboBox)edits[i]).setSelectedIndex(db.dbInt(fields[i])-mods[i]);}}}
		
	for (int i = 0; i < hand.length; i++) { // weapon hand dd
		if (db.dbInt("inventorytype") == hand[i]) {
			W_hand.setSelectedIndex(i);}}
				
	if (db.dbInt("subclass") < 10) { // wep type dd
		  W_type.setSelectedIndex(db.dbInt("subclass"));} 
	else {W_type.setSelectedIndex(db.dbInt("subclass") - 1);}
	
	for (int i = 0; i < socketarray.length ; i++) { // sockets dd
		if (db.dbInt(socketfield[i]) < 8) {
			  socketarray[i].setSelectedIndex(db.dbInt(socketfield[i]));}
		else {socketarray[i].setSelectedIndex(4);}}
		
	for (int i = 0; i < stattypes.length; i++) { // stats dd
		sdd = db.dbInt("stat_type"+i);
		if (sdd < 2) {
			stattypes[i].setSelectedIndex(sdd);}
		else {if (sdd < 12) {
				  stattypes[i].setSelectedIndex(sdd - 1);}
			  else {stattypes[i].setSelectedIndex(sdd - 5);}}}

	int importrace = db.dbInt("allowablerace"), importclass = db.dbInt("allowableclass");
	for (int i = 0 ; i < rcmod.length ; i++){ // race/class
		if (importrace - rcmod[i] >= 0){importrace-= rcmod[i];rmod[i].setSelected(true);}
		if (importclass - rcmod[i] >= 0){importclass-= rcmod[i];cmod[i].setSelected(true);}}
	if (importrace < 0) {R_allCB.doClick();}
	if (importclass < 0) {C_allCB.doClick();}
		
	INOUT.setText(db.getstatus());
	opentabs();}
	catch (Exception e){INOUT.setText("Import failed.\n\nTry reconnecting.\n\n"+e);}}

  ////////////
 // EXPORT //
////////////

public void executeExport() {
	try{
	db.exportprep(Integer.parseInt(NEWENTRYFIELD.getText()));
	
	NULLCB.setSelected(true); // basic fields
	for (int i = 0 ; i < checks.length ; i++){
		if (checks[i].isSelected()){
			if (edits[i] instanceof JTextField){
				db.dbExport(fields[i],((JTextField)edits[i]).getText());}
			else{db.dbExport(fields[i],digify(((JComboBox)edits[i]).getSelectedItem().toString()));}}}
	
	NULLCB.setSelected(false); // race/class
	int outrace=0, outclass=0;
	for (int i = 0 ; i < rcmod.length ; i++){
		if (rmod[i].isSelected()){outrace+=rcmod[i];}
		if (cmod[i].isSelected()){outclass+=rcmod[i];}}
	db.dbExport("allowablerace",outrace);
	db.dbExport("allowableclass",outclass);
				
	if (G_mapCB.isSelected()) { // reset restrictions
		for (String next : restrictions) {
			db.dbExport(next, 0);}}
			
	if (db.check()); { // send to table
		db.exportfinalize(G_name.getText(),NEWENTRYFIELD.getText());
		INOUT.setText(db.getstatus());}}
		
	catch (Exception e){INOUT.setText("Export failed.\n\n"+e);}}

public String digify(String text) {
    String output = "";
    for (int i = 0; i < text.length() ; i++) {        
        if (Character.isDigit(text.charAt(i))) {
            output += text.charAt(i);}}
    return output;}

  ////////////////
 // COMPONENTS //
////////////////

// GUI stuff
JPanel PB = new JPanel(),PB1 = new JPanel(),PB2 = new JPanel(),PB3 = new JPanel(),PB4 = new JPanel(),PB5 = new JPanel(),PF = new JPanel(),PG = new JPanel(),PH = new JPanel(),PI = new JPanel(),PJ = new JPanel(),PK = new JPanel(),PQ = new JPanel(),PS = new JPanel(),GROUP1 = new JPanel(),GROUP2 = new JPanel(),GROUP3 = new JPanel(),GROUP4 = new JPanel(),GROUP5 = new JPanel(),GROUP6 = new JPanel(),GROUP7 = new JPanel(),GROUP8 = new JPanel(),GROUP9 = new JPanel(),MASTER1 = new JPanel(),MASTER2 = new JPanel(),wepdmg1 = new JPanel(),wepdmg2 = new JPanel(),INOUTBOX = new JPanel();
JTabbedPane PC = new JTabbedPane(), wepdmg = new JTabbedPane();
JPasswordField MYSQLpass = new JPasswordField("root",13);
JButton CONNECTbutton = new JButton("<HTML><B><FONT COLOR=\"#BBBB00\">##</FONT> Connect <FONT COLOR=\"#BBBB00\">##</FONT>"),IMPORTbuttonA = new JButton("Lookup"),CREATEbuttonA = new JButton("Create"),importSEL = new JButton("Import"),HELPbutton = new JButton("Help");
String helptext = "To connect,enter your access info and click 'Connect'.\nThis 'item creator' works by building off of existing game items.  Pick an item you want to copy from to get the item type,icon,and model (pick a sword to make a sword,pick a bag to make a bag,etc),and type the name of that item into the 'Lookup by name' field,then click 'Lookup'.  Once the query has finished running,a drop-down box will appear with all items matching your search.  Select one and click 'import'.  This will enable the item info tabs,allowing you to edit the item's stats.  Once you have finished your adjustments,return to the main tab,enter a unique item ID to specify the item from ingame (cannot be a duplicate,so I advise using numbers greater than 50,000),and click 'Create'.  If everything goes right,the item will be injected to your database.\n\nCode by 711.  http://code.google.com/p/de4m/";
JTextArea INOUT = new JTextArea("Not currently connected.  \n" + helptext,12,53);
JTextField NEWENTRYFIELD = new JTextField(5), IMPORTTHIS = new JTextField(12), MYSQLdb = new JTextField("3306",13), MYSQLhost = new JTextField("127.0.0.1",13), MYSQLuser = new JTextField("root",13);
JCheckBox G_mapCB = new JCheckBox("Remove usage restrictions"), A_resistsCB = new JCheckBox("Resistances"), C_0CB = new JCheckBox("Druid"), C_1CB = new JCheckBox("Warrior"), C_2CB = new JCheckBox("Paladin"), C_3CB = new JCheckBox("Hunter"), C_4CB = new JCheckBox("Rogue"), C_5CB = new JCheckBox("Priest"), C_6CB = new JCheckBox("Death Knight"), C_7CB = new JCheckBox("Shaman"), C_8CB = new JCheckBox("Mage"), C_9CB = new JCheckBox("Warlock"), C_allCB = new JCheckBox("<HTML><B>All Classes</B>"), NULLCB = new JCheckBox(), R_0CB = new JCheckBox("Dranei"), R_1CB = new JCheckBox("Human"), R_2CB = new JCheckBox("Orc"), R_3CB = new JCheckBox("Dwarf"), R_4CB = new JCheckBox("Night Elf"), R_5CB = new JCheckBox("Undead"), R_6CB = new JCheckBox("Tauren"), R_7CB = new JCheckBox("Gnome"), R_8CB = new JCheckBox("Troll"), R_9CB = new JCheckBox("Blood Elf"), R_allCB = new JCheckBox("<HTML><B>All Races</B>");
JComboBox IMPORTDD = new JComboBox();

// editable content
Object[] ammooa = {"2-arrows","3-bullets"}, bondingoa = {"0-none","1-pickup","2-equip","3-use","4-quest"}, damtypeoa = {"0-physical","1-holy","2-fire","3-nature","4-frost","5-shadow","6-arcane"}, foodoa = {"1-meat","2-fish","3-cheese","4-bread","5-fungus","6-fruit","7-raw meat","8-raw fish"}, materialoa = {"1-metal","2-wood","3-liquid","4-jewelry","5-chain","6-plate","7-cloth","8-leather"}, qualityoa = {"0-grey","1-white","2-green","3-blue","4-purple","5-orange","6-red"}, sheathoa = {"1-back point down","2-back pointing up","3-side","4-back center","5-wand/rod","6-offhand side"}, socketsoa = {"0-none","1-meta","2-red","3-yellow","8-blue"}, statsoa = {"0-mana","1-health","3-agility","4-strength","5-intellect","6-spirit","7-stamina","12-defense","13-dodge","14-parry","15-block","16-hit melee","17-hit ranged","18-hit spell","19-crit melee","20-crit ranged","21-crit spell","22-hit taken m","23-hit taken r","24-hit taken s","25-crit taken m","26-crit taken r","27-crit taken s","28-haste melee","29-haste ranged","30-haste spell","31-hit","32-crit","33-hit taken","34-crit taken","35-resilience","36-haste","37-expertise"}, wephandoa = {"13-one hand","15-ranged","17-two hand",
	"21-main hand","22-off hand","25-throwing","26-ranged right"}, weptypeoa = {"0-1h axe","1-2h axe","2-bow","3-gun","4-1h mace","5-2h mace","6-polearm","7-1h sword","8-2h sword","10-staff","11-exotic","12-exotic","13-fist","14-misc","15-dagger","16-throwing","17-spear","18-crossbow","19-wand","20-fishing"};
JComboBox A_socket1 = new JComboBox(socketsoa), A_socket2 = new JComboBox(socketsoa), A_socket3 = new JComboBox(socketsoa), G_bonding = new JComboBox(bondingoa), G_quality = new JComboBox(qualityoa), M_food = new JComboBox(foodoa), M_material = new JComboBox(materialoa), S_0type = new JComboBox(statsoa), S_1type = new JComboBox(statsoa), S_2type = new JComboBox(statsoa), S_3type = new JComboBox(statsoa), S_4type = new JComboBox(statsoa), S_5type = new JComboBox(statsoa), S_6type = new JComboBox(statsoa), S_7type = new JComboBox(statsoa), S_8type = new JComboBox(statsoa), S_9type = new JComboBox(statsoa), W_ammo = new JComboBox(ammooa), W_damage2 = new JComboBox(damtypeoa), W_damage = new JComboBox(damtypeoa), W_hand = new JComboBox(wephandoa), W_sheath = new JComboBox(sheathoa), W_type = new JComboBox(weptypeoa);
JTextField A_arcane = new JTextField(4), A_armor = new JTextField(10), A_block = new JTextField(8), A_fire = new JTextField(4), A_frost = new JTextField(4), A_holy = new JTextField(4), A_nature = new JTextField(4), A_shadow = new JTextField(4), G_description = new JTextField(8), G_durability = new JTextField(8), G_level = new JTextField(8), G_name = new JTextField(17), M_buy = new JTextField(10), M_carry = new JTextField(8), M_sell = new JTextField(8), M_slots = new JTextField(8), M_stack = new JTextField(8),  S_0value = new JTextField(10), S_1value = new JTextField(8), S_2value = new JTextField(8), S_3value = new JTextField(8), S_4value = new JTextField(8), S_5value = new JTextField(8), S_6value = new JTextField(8), S_7value = new JTextField(8), S_8value = new JTextField(8), S_9value = new JTextField(8), W_max2 = new JTextField(8), W_maximum = new JTextField(8), W_min2 = new JTextField(8), W_minimum = new JTextField(8), W_range = new JTextField(8), W_speed = new JTextField(8);
JCheckBox A_armorCB = new JCheckBox(), A_blockCB = new JCheckBox(), A_socket1CB = new JCheckBox(), A_socket2CB = new JCheckBox(), A_socket3CB = new JCheckBox(), G_bondingCB = new JCheckBox(), G_descriptionCB = new JCheckBox(), G_durabilityCB = new JCheckBox(), G_levelCB = new JCheckBox(), G_nameCB = new JCheckBox(), G_qualityCB = new JCheckBox(), M_buyCB = new JCheckBox(), M_carryCB = new JCheckBox(), M_foodCB = new JCheckBox(), M_materialCB = new JCheckBox(), M_sellCB = new JCheckBox(), M_slotsCB = new JCheckBox(), M_stackCB = new JCheckBox(), W_ammoCB = new JCheckBox(), W_damage2CB = new JCheckBox(), W_damageCB = new JCheckBox(), W_handCB = new JCheckBox(), W_max2CB = new JCheckBox(), W_maximumCB = new JCheckBox(), W_min2CB = new JCheckBox(), W_minimumCB = new JCheckBox(), W_rangeCB = new JCheckBox(), W_sheathCB = new JCheckBox(), W_speedCB = new JCheckBox(), W_typeCB = new JCheckBox(); 
JLabel A_arcaneLabel = new JLabel("Arcane"), A_armorLabel = new JLabel("Armor"), A_blockLabel = new JLabel("Block"), A_fireLabel = new JLabel("Fire"), A_frostLabel = new JLabel("Frost"), A_holyLabel = new JLabel("Holy"), A_natureLabel = new JLabel("Nature"), A_shadowLabel = new JLabel("Shadow"), A_socket1Label = new JLabel("Socket1"), A_socket2Label = new JLabel("Socket2"), A_socket3Label = new JLabel("Socket3"), CREATElabel = new JLabel("New ID:"), G_bondingLabel = new JLabel("Bonding"), G_descriptionLabel = new JLabel("Description"), G_durabilityLabel = new JLabel("Dura"), G_levelLabel = new JLabel("LevelReq"), G_nameLabel = new JLabel("Name"), G_qualityLabel = new JLabel("Quality"), IMPORTlabel = new JLabel("Name:"), MYSQLdbLabel = new JLabel("<HTML><B>MYSQL port"), MYSQLhostLabel = new JLabel("<HTML><B>MYSQL host"), MYSQLpassLabel = new JLabel("<HTML><B>MYSQL password"), MYSQLuserLabel = new JLabel("<HTML><B>MYSQL username"), M_buyLabel = new JLabel("Buy Price"), M_carryLabel = new JLabel("MaxCarry"), M_foodLabel = new JLabel("FoodType"), M_materialLabel = new JLabel("Material"), M_sellLabel = new JLabel("Sell Price"), M_slotsLabel = new JLabel("BagSlots"), 
	M_stackLabel = new JLabel("MaxStack"), S_0Label = new JLabel("modify by:"), S_1Label = new JLabel("modify by:"), S_2Label = new JLabel("modify by:"), S_3Label = new JLabel("modify by;"), S_4Label = new JLabel("modify by:"), S_5Label = new JLabel("modify by:"), S_6Label = new JLabel("modify by:"), S_7Label = new JLabel("modify by:"), S_8Label = new JLabel("modify by:"), S_9Label = new JLabel("modify by:"), W_ammoLabel = new JLabel("Ammo"), W_damage2Label = new JLabel("DmgType"), W_damageLabel = new JLabel("DmgType"), W_handLabel = new JLabel("Hand"), W_max2Label = new JLabel("MaxDmg"), W_maximumLabel = new JLabel("MaxDmg"), W_min2Label = new JLabel("MinDmg"), W_minimumLabel = new JLabel("MinDmg"), W_rangeLabel = new JLabel("Range"), W_sheathLabel = new JLabel("Sheath"), W_speedLabel = new JLabel("Speed"), W_typeLabel = new JLabel("Type"), aseclabel = new JLabel("<HTML><B>ARMOR"), gseclabel = new JLabel("<HTML><B>GENERAL"), mseclabel = new JLabel("<HTML><B>MISC"), rseclabel = new JLabel("<HTML><B>RACE/CLASS"), sseclabel = new JLabel("<HTML><B>STATS"), wseclabel = new JLabel("<HTML><B>WEAPONS");

// import/export helpers
JCheckBox[] CBarray = {R_1CB,R_2CB,R_3CB,R_4CB,R_5CB,R_6CB,R_7CB,R_8CB,R_9CB,R_0CB,C_1CB,C_2CB,C_3CB,C_4CB,C_5CB,C_6CB,C_7CB,C_8CB,C_9CB,C_0CB,G_nameCB,G_descriptionCB,G_levelCB,G_durabilityCB,W_minimumCB,W_maximumCB,W_speedCB,A_armorCB,A_blockCB,M_buyCB,M_sellCB,M_carryCB,M_stackCB,M_slotsCB,G_qualityCB,G_bondingCB,W_typeCB,W_handCB,W_damageCB,W_ammoCB,W_sheathCB,A_socket1CB,A_socket2CB,A_socket3CB,M_materialCB,M_foodCB,G_mapCB,R_allCB,C_allCB,A_resistsCB,W_damage2CB,W_min2CB,W_max2CB,W_rangeCB};
String[] restrictions = {"map","area","duration","requiredspell","requiredskillrank","requiredhonorrank","requiredcityrank","requiredreputationfaction","requiredreputationrank"};
JCheckBox[] checks = {A_armorCB, A_blockCB, G_bondingCB, G_descriptionCB, G_durabilityCB, G_levelCB, G_nameCB, G_qualityCB, M_buyCB, M_carryCB, M_foodCB, M_materialCB, M_sellCB, M_slotsCB, M_stackCB, W_ammoCB, W_damage2CB, W_damageCB, W_max2CB, W_maximumCB, W_min2CB, W_minimumCB, W_rangeCB, W_sheathCB, W_speedCB, NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,A_resistsCB,A_resistsCB,A_resistsCB,A_resistsCB,A_resistsCB,A_resistsCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,NULLCB,A_socket1CB,A_socket2CB,A_socket3CB,W_typeCB,W_handCB};
Object[] edits = {A_armor, A_block, G_bonding, G_description, G_durability, G_level, G_name, G_quality, M_buy, M_carry, M_food, M_material, M_sell, M_slots, M_stack, W_ammo, W_damage2, W_damage, W_max2, W_maximum, W_min2, W_minimum, W_range, W_sheath, W_speed, S_0value,S_1value,S_2value,S_3value,S_4value,S_5value,S_6value,S_7value,S_8value,S_9value,A_arcane,A_holy,A_fire,A_frost,A_shadow,A_nature,S_0type,S_1type,S_2type,S_3type,S_4type,S_5type,S_6type,S_7type,S_8type,S_9type,A_socket1,A_socket2,A_socket3,W_type,W_hand};
String[] fields = {"armor","block","bonding","description","maxdurability","requiredlevel","name","quality","buyprice","maxcount","food_type","material","sellprice","containerslots","stackable","ammo_type","dmg_type2","dmg_type1","dmg_max2","dmg_max1","dmg_min2","dmg_min1","rangedmodrange","sheath","delay","stat_value1","stat_value2","stat_value3","stat_value4","stat_value5","stat_value6","stat_value7","stat_value8","stat_value9","stat_value10","arcane_res","holy_res","fire_res","frost_res","shadow_res","nature_res","stat_type1","stat_type2","stat_type3","stat_type4","stat_type5","stat_type6","stat_type7","stat_type8","stat_type9","stat_type10","socketcolor_1","socketcolor_2","socketcolor_3","subclass","inventorytype"};
int[] mods = {0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,2,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
int[] hand = {13,15,17,21,22,25,26};
JComboBox[] socketarray = {A_socket1,A_socket2,A_socket3};
String[] socketfield = {"socketColor_1","socketColor_2","socketColor_3"};
JComboBox[] stattypes = {S_9type,S_0type,S_1type,S_2type,S_3type,S_4type,S_5type,S_6type,S_7type,S_8type};
int[] rcmod = {1024,512,256,128,64,32,16,8,4,2,1};
JCheckBox[] rmod = {R_0CB, R_9CB, NULLCB, R_8CB, R_7CB, R_6CB, R_5CB, R_4CB, R_3CB, R_2CB, R_1CB};
JCheckBox[] cmod = {C_0CB, NULLCB, C_9CB, C_8CB, C_7CB, C_6CB, C_5CB, C_4CB, C_3CB, C_2CB, C_1CB};

public void buildgroups(){
	// COMPONENT GRIDS
	GroupLayout GROUP1LAYOUT = new GroupLayout(GROUP1);GROUP1.setLayout(GROUP1LAYOUT);
		GROUP1LAYOUT.setHorizontalGroup(GROUP1LAYOUT.createSequentialGroup().addGroup(GROUP1LAYOUT.createParallelGroup().addComponent(MYSQLhostLabel).addComponent(MYSQLhost)).addGroup(GROUP1LAYOUT.createParallelGroup().addComponent(MYSQLdbLabel).addComponent(MYSQLdb)).addGroup(GROUP1LAYOUT.createParallelGroup().addComponent(MYSQLuserLabel).addComponent(MYSQLuser)).addGroup(GROUP1LAYOUT.createParallelGroup().addComponent(MYSQLpassLabel).addComponent(MYSQLpass)));
		GROUP1LAYOUT.setVerticalGroup(GROUP1LAYOUT.createSequentialGroup().addGroup(GROUP1LAYOUT.createParallelGroup().addComponent(MYSQLhostLabel).addComponent(MYSQLdbLabel).addComponent(MYSQLuserLabel).addComponent(MYSQLpassLabel)).addGroup(GROUP1LAYOUT.createParallelGroup().addComponent(MYSQLhost).addComponent(MYSQLdb).addComponent(MYSQLuser).addComponent(MYSQLpass)));
	GroupLayout GROUP2LAYOUT = new GroupLayout(GROUP2);GROUP2.setLayout(GROUP2LAYOUT);GROUP2LAYOUT.setAutoCreateGaps(true);
        GROUP2LAYOUT.setHorizontalGroup(GROUP2LAYOUT.createSequentialGroup().addGroup(GROUP2LAYOUT.createParallelGroup().addComponent(G_nameLabel).addComponent(G_descriptionLabel).addComponent(G_levelLabel).addComponent(G_durabilityLabel).addComponent(G_qualityLabel).addComponent(G_bondingLabel)).addGroup(GROUP2LAYOUT.createParallelGroup().addComponent(G_name).addComponent(G_description).addComponent(G_level).addComponent(G_durability).addComponent(G_quality).addComponent(G_bonding)).addGroup(GROUP2LAYOUT.createParallelGroup().addComponent(G_nameCB).addComponent(G_descriptionCB).addComponent(G_levelCB).addComponent(G_durabilityCB).addComponent(G_qualityCB).addComponent(G_bondingCB)));
        GROUP2LAYOUT.setVerticalGroup(GROUP2LAYOUT.createSequentialGroup().addGroup(GROUP2LAYOUT.createParallelGroup().addComponent(G_nameLabel).addComponent(G_name).addComponent(G_nameCB)).addGroup(GROUP2LAYOUT.createParallelGroup().addComponent(G_descriptionLabel).addComponent(G_description).addComponent(G_descriptionCB)).addGroup(GROUP2LAYOUT.createParallelGroup().addComponent(G_levelLabel).addComponent(G_level).addComponent(G_levelCB)).addGroup(GROUP2LAYOUT.createParallelGroup().addComponent(G_durabilityLabel).addComponent(G_durability).addComponent(G_durabilityCB)).addGroup(GROUP2LAYOUT.createParallelGroup().addComponent(G_qualityLabel).addComponent(G_quality).addComponent(G_qualityCB)).addGroup(GROUP2LAYOUT.createParallelGroup().addComponent(G_bondingLabel).addComponent(G_bonding).addComponent(G_bondingCB)));
    GroupLayout GROUP3LAYOUT = new GroupLayout(GROUP3);GROUP3.setLayout(GROUP3LAYOUT);GROUP3LAYOUT.setAutoCreateGaps(true);
        GROUP3LAYOUT.setHorizontalGroup(GROUP3LAYOUT.createSequentialGroup().addGroup(GROUP3LAYOUT.createParallelGroup().addComponent(W_typeLabel).addComponent(W_handLabel).addComponent(W_sheathLabel).addComponent(W_ammoLabel).addComponent(W_speedLabel).addComponent(W_rangeLabel)).addGroup(GROUP3LAYOUT.createParallelGroup().addComponent(W_type).addComponent(W_hand).addComponent(W_sheath).addComponent(W_ammo).addComponent(W_speed).addComponent(W_range)).addGroup(GROUP3LAYOUT.createParallelGroup().addComponent(W_typeCB).addComponent(W_handCB).addComponent(W_sheathCB).addComponent(W_ammoCB).addComponent(W_speedCB).addComponent(W_rangeCB)));
        GROUP3LAYOUT.setVerticalGroup(GROUP3LAYOUT.createSequentialGroup().addGroup(GROUP3LAYOUT.createParallelGroup().addComponent(W_typeLabel).addComponent(W_type).addComponent(W_typeCB)).addGroup(GROUP3LAYOUT.createParallelGroup().addComponent(W_handLabel).addComponent(W_hand).addComponent(W_handCB)).addGroup(GROUP3LAYOUT.createParallelGroup().addComponent(W_sheathLabel).addComponent(W_sheath).addComponent(W_sheathCB)).addGroup(GROUP3LAYOUT.createParallelGroup().addComponent(W_ammoLabel).addComponent(W_ammo).addComponent(W_ammoCB)).addGroup(GROUP3LAYOUT.createParallelGroup().addComponent(W_speedLabel).addComponent(W_speed).addComponent(W_speedCB)).addGroup(GROUP3LAYOUT.createParallelGroup().addComponent(W_rangeLabel).addComponent(W_range).addComponent(W_rangeCB)));
    GroupLayout wepdmg1layout = new GroupLayout(wepdmg1);wepdmg1.setLayout(wepdmg1layout);wepdmg1layout.setAutoCreateGaps(true);
		wepdmg1layout.setHorizontalGroup(wepdmg1layout.createSequentialGroup().addGroup(wepdmg1layout.createParallelGroup().addComponent(W_damageLabel).addComponent(W_minimumLabel).addComponent(W_maximumLabel)).addGroup(wepdmg1layout.createParallelGroup().addComponent(W_damage).addComponent(W_minimum).addComponent(W_maximum)).addGroup(wepdmg1layout.createParallelGroup().addComponent(W_damageCB).addComponent(W_minimumCB).addComponent(W_maximumCB)));
		wepdmg1layout.setVerticalGroup(wepdmg1layout.createSequentialGroup().addGroup(wepdmg1layout.createParallelGroup().addComponent(W_damageLabel).addComponent(W_damage).addComponent(W_damageCB)).addGroup(wepdmg1layout.createParallelGroup().addComponent(W_minimumLabel).addComponent(W_minimum).addComponent(W_minimumCB)).addGroup(wepdmg1layout.createParallelGroup().addComponent(W_maximumLabel).addComponent(W_maximum).addComponent(W_maximumCB)));
	GroupLayout wepdmg2layout = new GroupLayout(wepdmg2);wepdmg2.setLayout(wepdmg2layout);wepdmg2layout.setAutoCreateGaps(true);
		wepdmg2layout.setHorizontalGroup(wepdmg2layout.createSequentialGroup().addGroup(wepdmg2layout.createParallelGroup().addComponent(W_damage2Label).addComponent(W_min2Label).addComponent(W_max2Label)).addGroup(wepdmg2layout.createParallelGroup().addComponent(W_damage2).addComponent(W_min2).addComponent(W_max2)).addGroup(wepdmg2layout.createParallelGroup().addComponent(W_damage2CB).addComponent(W_min2CB).addComponent(W_max2CB)));
		wepdmg2layout.setVerticalGroup(wepdmg2layout.createSequentialGroup().addGroup(wepdmg2layout.createParallelGroup().addComponent(W_damage2Label).addComponent(W_damage2).addComponent(W_damage2CB)).addGroup(wepdmg2layout.createParallelGroup().addComponent(W_min2Label).addComponent(W_min2).addComponent(W_min2CB)).addGroup(wepdmg2layout.createParallelGroup().addComponent(W_max2Label).addComponent(W_max2).addComponent(W_max2CB)));
	GroupLayout GROUP4LAYOUT = new GroupLayout(GROUP4);GROUP4.setLayout(GROUP4LAYOUT);GROUP4LAYOUT.setAutoCreateGaps(true);
        GROUP4LAYOUT.setHorizontalGroup(GROUP4LAYOUT.createSequentialGroup().addGroup(GROUP4LAYOUT.createParallelGroup().addComponent(A_armorLabel).addComponent(A_blockLabel).addComponent(A_socket1Label).addComponent(A_socket2Label).addComponent(A_socket3Label)).addGroup(GROUP4LAYOUT.createParallelGroup().addComponent(A_armor).addComponent(A_block).addComponent(A_socket1).addComponent(A_socket2).addComponent(A_socket3)).addGroup(GROUP4LAYOUT.createParallelGroup().addComponent(A_armorCB).addComponent(A_blockCB).addComponent(A_socket1CB).addComponent(A_socket2CB).addComponent(A_socket3CB)));
        GROUP4LAYOUT.setVerticalGroup(GROUP4LAYOUT.createSequentialGroup().addGroup(GROUP4LAYOUT.createParallelGroup().addComponent(A_armorLabel).addComponent(A_armor).addComponent(A_armorCB)).addGroup(GROUP4LAYOUT.createParallelGroup().addComponent(A_blockLabel).addComponent(A_block).addComponent(A_blockCB)).addGroup(GROUP4LAYOUT.createParallelGroup().addComponent(A_socket1Label).addComponent(A_socket1).addComponent(A_socket1CB)).addGroup(GROUP4LAYOUT.createParallelGroup().addComponent(A_socket2Label).addComponent(A_socket2).addComponent(A_socket2CB)).addGroup(GROUP4LAYOUT.createParallelGroup().addComponent(A_socket3Label).addComponent(A_socket3).addComponent(A_socket3CB)));
    GroupLayout GROUP5LAYOUT = new GroupLayout(GROUP5);GROUP5.setLayout(GROUP5LAYOUT);GROUP5LAYOUT.setAutoCreateGaps(true);
        GROUP5LAYOUT.setHorizontalGroup(GROUP5LAYOUT.createSequentialGroup().addGroup(GROUP5LAYOUT.createParallelGroup().addComponent(A_arcaneLabel).addComponent(A_fireLabel).addComponent(A_frostLabel)).addGroup(GROUP5LAYOUT.createParallelGroup().addComponent(A_arcane).addComponent(A_fire).addComponent(A_frost)).addGroup(GROUP5LAYOUT.createParallelGroup().addComponent(A_holyLabel).addComponent(A_natureLabel).addComponent(A_shadowLabel)).addGroup(GROUP5LAYOUT.createParallelGroup().addComponent(A_holy).addComponent(A_nature).addComponent(A_shadow)));
        GROUP5LAYOUT.setVerticalGroup(GROUP5LAYOUT.createSequentialGroup().addGroup(GROUP5LAYOUT.createParallelGroup().addComponent(A_arcaneLabel).addComponent(A_arcane).addComponent(A_holyLabel).addComponent(A_holy)).addGroup(GROUP5LAYOUT.createParallelGroup().addComponent(A_fireLabel).addComponent(A_fire).addComponent(A_natureLabel).addComponent(A_nature)).addGroup(GROUP5LAYOUT.createParallelGroup().addComponent(A_frostLabel).addComponent(A_frost).addComponent(A_shadowLabel).addComponent(A_shadow)));
    GroupLayout GROUP6LAYOUT = new GroupLayout(GROUP6);GROUP6.setLayout(GROUP6LAYOUT);GROUP6LAYOUT.setAutoCreateGaps(true);
        GROUP6LAYOUT.setHorizontalGroup(GROUP6LAYOUT.createSequentialGroup().addGroup(GROUP6LAYOUT.createParallelGroup().addComponent(M_buyLabel).addComponent(M_sellLabel).addComponent(M_carryLabel).addComponent(M_stackLabel).addComponent(M_slotsLabel).addComponent(M_materialLabel).addComponent(M_foodLabel)).addGroup(GROUP6LAYOUT.createParallelGroup().addComponent(M_buy).addComponent(M_sell).addComponent(M_carry).addComponent(M_stack).addComponent(M_slots).addComponent(M_material).addComponent(M_food)).addGroup(GROUP6LAYOUT.createParallelGroup().addComponent(M_buyCB).addComponent(M_sellCB).addComponent(M_carryCB).addComponent(M_stackCB).addComponent(M_slotsCB).addComponent(M_materialCB).addComponent(M_foodCB)));
        GROUP6LAYOUT.setVerticalGroup(GROUP6LAYOUT.createSequentialGroup().addGroup(GROUP6LAYOUT.createParallelGroup().addComponent(M_buyLabel).addComponent(M_buy).addComponent(M_buyCB)).addGroup(GROUP6LAYOUT.createParallelGroup().addComponent(M_sellLabel).addComponent(M_sell).addComponent(M_sellCB)).addGroup(GROUP6LAYOUT.createParallelGroup().addComponent(M_carryLabel).addComponent(M_carry).addComponent(M_carryCB)).addGroup(GROUP6LAYOUT.createParallelGroup().addComponent(M_stackLabel).addComponent(M_stack).addComponent(M_stackCB)).addGroup(GROUP6LAYOUT.createParallelGroup().addComponent(M_slotsLabel).addComponent(M_slots).addComponent(M_slotsCB)).addGroup(GROUP6LAYOUT.createParallelGroup().addComponent(M_materialLabel).addComponent(M_material).addComponent(M_materialCB)).addGroup(GROUP6LAYOUT.createParallelGroup().addComponent(M_foodLabel).addComponent(M_food).addComponent(M_foodCB)));
    GroupLayout GROUPLAYOUT7 = new GroupLayout(GROUP7);GROUP7.setLayout(GROUPLAYOUT7);GROUPLAYOUT7.setAutoCreateGaps(true);
        GROUPLAYOUT7.setHorizontalGroup(GROUPLAYOUT7.createSequentialGroup().addGroup(GROUPLAYOUT7.createParallelGroup().addComponent(R_1CB).addComponent(R_3CB).addComponent(R_4CB).addComponent(R_7CB).addComponent(R_0CB)).addGroup(GROUPLAYOUT7.createParallelGroup().addComponent(R_2CB).addComponent(R_5CB).addComponent(R_6CB).addComponent(R_8CB).addComponent(R_9CB)));
        GROUPLAYOUT7.setVerticalGroup(GROUPLAYOUT7.createSequentialGroup().addGroup(GROUPLAYOUT7.createParallelGroup().addComponent(R_1CB).addComponent(R_2CB)).addGroup(GROUPLAYOUT7.createParallelGroup().addComponent(R_3CB).addComponent(R_5CB)).addGroup(GROUPLAYOUT7.createParallelGroup().addComponent(R_4CB).addComponent(R_6CB)).addGroup(GROUPLAYOUT7.createParallelGroup().addComponent(R_7CB).addComponent(R_8CB)).addGroup(GROUPLAYOUT7.createParallelGroup().addComponent(R_0CB).addComponent(R_9CB)));
    GroupLayout GROUP8LAYOUT = new GroupLayout(GROUP8);GROUP8.setLayout(GROUP8LAYOUT);GROUP8LAYOUT.setAutoCreateGaps(true);
        GROUP8LAYOUT.setHorizontalGroup(GROUP8LAYOUT.createSequentialGroup().addGroup(GROUP8LAYOUT.createParallelGroup().addComponent(C_1CB).addComponent(C_3CB).addComponent(C_4CB).addComponent(C_7CB).addComponent(C_0CB)).addGroup(GROUP8LAYOUT.createParallelGroup().addComponent(C_2CB).addComponent(C_5CB).addComponent(C_6CB).addComponent(C_8CB).addComponent(C_9CB)));
        GROUP8LAYOUT.setVerticalGroup(GROUP8LAYOUT.createSequentialGroup().addGroup(GROUP8LAYOUT.createParallelGroup().addComponent(C_1CB).addComponent(C_2CB)).addGroup(GROUP8LAYOUT.createParallelGroup().addComponent(C_3CB).addComponent(C_5CB)).addGroup(GROUP8LAYOUT.createParallelGroup().addComponent(C_4CB).addComponent(C_6CB)).addGroup(GROUP8LAYOUT.createParallelGroup().addComponent(C_7CB).addComponent(C_8CB)).addGroup(GROUP8LAYOUT.createParallelGroup().addComponent(C_0CB).addComponent(C_9CB)));
    GroupLayout GROUP9LAYOUT = new GroupLayout(GROUP9);GROUP9.setLayout(GROUP9LAYOUT);GROUP9LAYOUT.setAutoCreateGaps(true);
        GROUP9LAYOUT.setHorizontalGroup(GROUP9LAYOUT.createSequentialGroup().addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_0type).addComponent(S_1type).addComponent(S_2type).addComponent(S_3type).addComponent(S_4type).addComponent(S_5type).addComponent(S_6type).addComponent(S_7type).addComponent(S_8type).addComponent(S_9type)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_0Label).addComponent(S_1Label).addComponent(S_2Label).addComponent(S_3Label).addComponent(S_4Label).addComponent(S_5Label).addComponent(S_6Label).addComponent(S_7Label).addComponent(S_8Label).addComponent(S_9Label)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_0value).addComponent(S_1value).addComponent(S_2value).addComponent(S_3value).addComponent(S_4value).addComponent(S_5value).addComponent(S_6value).addComponent(S_7value).addComponent(S_8value).addComponent(S_9value)));
        GROUP9LAYOUT.setVerticalGroup(GROUP9LAYOUT.createSequentialGroup().addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_0type).addComponent(S_0Label).addComponent(S_0value)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_1type).addComponent(S_1Label).addComponent(S_1value)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_2type).addComponent(S_2Label).addComponent(S_2value)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_3type).addComponent(S_3Label).addComponent(S_3value)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_4type).addComponent(S_4Label).addComponent(S_4value)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_5type).addComponent(S_5Label).addComponent(S_5value)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_6type).addComponent(S_6Label).addComponent(S_6value)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_7type).addComponent(S_7Label).addComponent(S_7value)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_8type).addComponent(S_8Label).addComponent(S_8value)).addGroup(GROUP9LAYOUT.createParallelGroup().addComponent(S_9type).addComponent(S_9Label).addComponent(S_9value)));
	// STRAIGHT-GROUPS
	GroupLayout PBLAYOUT = new GroupLayout(PB);PB.setLayout(PBLAYOUT);
		PBLAYOUT.setHorizontalGroup(PBLAYOUT.createSequentialGroup().addGroup(PBLAYOUT.createParallelGroup().addComponent(GROUP1)).addGroup(PBLAYOUT.createParallelGroup().addComponent(PB5)).addGroup(PBLAYOUT.createParallelGroup().addComponent(PB2)).addGroup(PBLAYOUT.createParallelGroup().addComponent(INOUTBOX)));
		PBLAYOUT.setVerticalGroup(PBLAYOUT.createSequentialGroup().addGroup(PBLAYOUT.createParallelGroup().addComponent(GROUP1).addComponent(PB5).addComponent(PB2).addComponent(INOUTBOX)));
	GroupLayout PHLAYOUT = new GroupLayout(PH);PH.setLayout(PHLAYOUT);
		PHLAYOUT.setHorizontalGroup(PHLAYOUT.createSequentialGroup().addGroup(PHLAYOUT.createParallelGroup().addComponent(gseclabel).addComponent(GROUP2).addComponent(G_mapCB)));
		PHLAYOUT.setVerticalGroup(PHLAYOUT.createSequentialGroup().addGroup(PHLAYOUT.createParallelGroup().addComponent(gseclabel)).addGroup(PHLAYOUT.createParallelGroup().addComponent(GROUP2)).addGroup(PHLAYOUT.createParallelGroup().addComponent(G_mapCB)));
	GroupLayout PILAYOUT = new GroupLayout(PI);PI.setLayout(PILAYOUT);
		PILAYOUT.setHorizontalGroup(PILAYOUT.createSequentialGroup().addGroup(PILAYOUT.createParallelGroup().addComponent(wseclabel).addComponent(GROUP3).addComponent(wepdmg)));
		PILAYOUT.setVerticalGroup(PILAYOUT.createSequentialGroup().addGroup(PILAYOUT.createParallelGroup().addComponent(wseclabel)).addGroup(PILAYOUT.createParallelGroup().addComponent(GROUP3)).addGroup(PILAYOUT.createParallelGroup().addComponent(wepdmg)));
	GroupLayout PJLAYOUT = new GroupLayout(PJ);PJ.setLayout(PJLAYOUT);
		PJLAYOUT.setHorizontalGroup(PJLAYOUT.createSequentialGroup().addGroup(PJLAYOUT.createParallelGroup().addComponent(aseclabel).addComponent(GROUP4).addComponent(A_resistsCB).addComponent(GROUP5)));
		PJLAYOUT.setVerticalGroup(PJLAYOUT.createSequentialGroup().addGroup(PJLAYOUT.createParallelGroup().addComponent(aseclabel)).addGroup(PJLAYOUT.createParallelGroup().addComponent(GROUP4)).addGroup(PJLAYOUT.createParallelGroup().addComponent(A_resistsCB)).addGroup(PJLAYOUT.createParallelGroup().addComponent(GROUP5)));
	GroupLayout PKLAYOUT = new GroupLayout(PK);PK.setLayout(PKLAYOUT);
		PKLAYOUT.setHorizontalGroup(PKLAYOUT.createSequentialGroup().addGroup(PKLAYOUT.createParallelGroup().addComponent(mseclabel).addComponent(GROUP6)));
		PKLAYOUT.setVerticalGroup(PKLAYOUT.createSequentialGroup().addGroup(PKLAYOUT.createParallelGroup().addComponent(mseclabel)).addGroup(PKLAYOUT.createParallelGroup().addComponent(GROUP6)));
	GroupLayout PQLAYOUT = new GroupLayout(PQ);PQ.setLayout(PQLAYOUT);
		PQLAYOUT.setHorizontalGroup(PQLAYOUT.createSequentialGroup().addGroup(PQLAYOUT.createParallelGroup().addComponent(R_allCB).addComponent(GROUP7).addComponent(C_allCB).addComponent(GROUP8)));
		PQLAYOUT.setVerticalGroup(PQLAYOUT.createSequentialGroup().addGroup(PQLAYOUT.createParallelGroup().addComponent(R_allCB)).addGroup(PQLAYOUT.createParallelGroup().addComponent(GROUP7)).addGroup(PQLAYOUT.createParallelGroup().addComponent(C_allCB)).addGroup(PQLAYOUT.createParallelGroup().addComponent(GROUP8)));
	GroupLayout PSLAYOUT = new GroupLayout(PS);PS.setLayout(PSLAYOUT);
		PSLAYOUT.setHorizontalGroup(PSLAYOUT.createSequentialGroup().addGroup(PSLAYOUT.createParallelGroup().addComponent(sseclabel).addComponent(GROUP9)));
		PSLAYOUT.setVerticalGroup(PSLAYOUT.createSequentialGroup().addGroup(PSLAYOUT.createParallelGroup().addComponent(sseclabel)).addGroup(PSLAYOUT.createParallelGroup().addComponent(GROUP9)));
	GroupLayout PB5LAYOUT = new GroupLayout(PB5);PB5.setLayout(PB5LAYOUT);
		PB5LAYOUT.setHorizontalGroup(PB5LAYOUT.createSequentialGroup().addGroup(PB5LAYOUT.createParallelGroup().addComponent(PB3)).addGroup(PB5LAYOUT.createParallelGroup().addComponent(PB4)));
		PB5LAYOUT.setVerticalGroup(PB5LAYOUT.createSequentialGroup().addGroup(PB5LAYOUT.createParallelGroup().addComponent(PB3).addComponent(PB4)));}}