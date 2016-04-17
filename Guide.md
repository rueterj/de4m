  * Deform doesn't require installation, however you WILL need java runtime environment to be able to use it.  There's a good chance you already do have it, though, and Deform will let you know if you don't.

  * MySQL must be running in order to use Deform, and you must have a database called 'mangos' with a table named 'item\_template'.  This is default for all '711' repacks, so if you're using one of those, you needn't worry.

  * The program works by importing existing items of the type you wish to use (import a sword to make a sword that looks just like it, import a bag to make a bag, etc).  Once connected to the database (press 'Connect' after typing in your access info), type an item name (full or partial) into the field next to the 'Lookup' button, and then press 'Lookup'.

  * Once you have selected an item from the drop-down box of items that match your search, click 'Import', and all that item's attributes will be imported for editing.  Change the attributes you would like to be different, and click the checkboxes next to those fields so that the program knows to export the item with those values changed.

  * Return to the main tab and entry a new ID number for the item (for UDB databases, numbers over 50000 aren't used by default, so choosing a number over 50000 is a good idea) and click 'Export'.  If all goes well, the item will be injected into the database and be ready to use.