package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.mover.CatalogMoverException;

public interface CatalogMoverCommand {

    void execute() throws CatalogMoverException;

}
