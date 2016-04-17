# Deform #
<table cellspacing='5'><tr><td>For now, Deform (<b>D</b> atabase <b>e</b> ditor <b>for M</b> aNGOS) is a basic JAVA based form-style database editor for importing, editing, and exporting items from an existing MaNGOS database (such as the kind provided by the UDB project).  The goal of this project is to provide an easy to use and available method for quickly altering and inserting custom items (and eventually other object types) into an existing MaNGOS database, without the use of web-based generators or general purpose sql tools.  At the moment, this project is an open source learning endeavor, not seeking contributors.  Feedback should be posted at <a href='http://www.mmowned.com/forums/programs/272962-alpha-mangos-item-creator-editor.html'>the MMOwned thread concerning this project</a>.</td><td width='150'></td></tr></table>
<img src='http://img99.imageshack.us/img99/6219/deform.gif'>

<h2>Downloads</h2>
<ul><li><b><a href='http://de4m.googlecode.com/svn/trunk/bin/deform.beta.rc1.exe'>deform.beta.rc1.exe</a></b>
</li><li><i><a href='http://www.java.com/en/download/manual.jsp'>Java Runtime Environment</a></i>
</li><li><i><a href='http://code.google.com/p/de4m/source/browse/#svn/trunk/bin'>All Versions</a></i>
</li><li><i><a href='http://code.google.com/p/de4m/source/browse/#svn/trunk'>Source Code</a></i></li></ul>

<h2>User Guide</h2>
<ul><li><b>Deform doesn't require installation,</b> however you WILL need java runtime environment to be able to use it.  There's a good chance you already do have it, though, and Deform will let you know if you don't.<br>
</li><li><b>MySQL must be running in order to use Deform,</b> and you must have a properly formatted item_template table (though the name might be different) in the specified database.<br>
</li><li><b>Deform works by importing existing items</b> of the type you wish to use (import a sword to make a sword that looks just like it, import a bag to make a bag, etc).  Once connected to the database (press 'Connect' after typing in your access info), type an item name (full or partial) into the field next to the 'Lookup' button, and then press 'Lookup'.<br>
</li><li>Once you have selected an item from the drop-down box of items that match your search, click 'Import', and all that item's attributes will be imported for editing.  Change the attributes you would like to be different, and click the checkboxes next to those fields so that the program knows to export the item with those values changed.<br>
</li><li>Return to the main tab and entry a new ID number for the item (for UDB databases, numbers over 50000 aren't used by default, so choosing a number over 50000 is a good idea) and click 'Export'.  If all goes well, the item will be injected into the database and be ready to use.</li></ul>

<h2>Features</h2>
<h4>Current Features</h4>
<ul><li>Direct database connection; no copying and pasting into SQLyog<br>
</li><li>Simple and friendly user interface; no need for reference guides<br>
</li><li>Import existing items from database for editing<br>
</li><li>Edit over 50 different item attributes<br>
</li><li>Export items into database or to file with desired ID<br>
</li><li>Quickly remove item usage restrictions<br>
</li><li>Irrelevant attribute fields are automatically hidden<br>
<h4>TO-DO list</h4>
</li><li>auto-set checkboxes on field edit<br>
<h4>Long term goals</h4>
</li><li>spells tab<br>
</li><li>quest editor<br>
</li><li>guaranteed support for multiple versions<br>
</li><li>instead of checkboxes, color code the attribute fields</li></ul>

<h2>Changelog</h2>
<h4>beta RC1</h4>
<ul><li>item name now displayed in window title<br>
</li><li>added ability to export items to file<br>
</li><li>added field for database selection, and table selector<br>
</li><li>unused attribute fields will now be automatically hidden or disabled<br>
</li><li>tabs rearranged so that attributes which apply to every item type are all on one page<br>
<h4>alpha 0.9</h4>
</li><li>invalid numerical input will now be automatically corrected<br>
</li><li>error messages for exports significantly improved<br>
<h4>alpha 0.8</h4>
</li><li>fixed names and descriptions not exporting correctly<br>
</li><li>added armor type dropdown<br>
<h4>alpha 0.7</h4>
</li><li>fixed minor code typo causing socket3 not to export<br>
</li><li>gave project new official name, introduced to Google Code<br>
<h4>alpha 0.6</h4>
</li><li>huge backend overhaul: much improved database interfacing<br>
</li><li>checkboxes will now all turn off on new import, to prevent accidental export of unwanted fields<br>
</li><li>certain common error messages improved<br>
<h4>alpha 0.5</h4>
</li><li>agility modifiers now import/export correctly<br>
</li><li>races/classes no longer get an extra "plus one" on export<br>
</li><li>help button should now work consistently<br>
</li><li>minor gui error fixes<br>
</li><li>map restriction resetter now resets ALL requirements (reputation, faction, map, skill, etc)<br>
</li><li>weapons panel gets new tabbed damage editor allowing for dmg1 and dmg2 to both be edited, and also gets weapon range field<br>
</li><li>minor GUI tweaks to make sure things aren't as ugly<br>
<h4>alpha 0.4</h4>
</li><li>add detail to UI<br>
</li><li>tidy up UI errors (UI now mostly as it should be)<br>
<h4>alpha 0.3</h4>
</li><li>major GUI tweaks<br>
</li><li>slightly improved error messages<br>
</li><li>major backend cleanup<br>
<h4>alpha 0.2</h4>
</li><li>remove dialogs for import/export, place directly on GUI<br>
</li><li>major backend cleanup (reduces filesize)<br>
<h4>alpha 0.1</h4>
</li><li>first version