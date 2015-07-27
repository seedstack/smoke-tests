/*
 * Creation : 30 juin 2015
 */
package org.seedstack.seed.jpa.websphere;

import org.hibernate.jpa.boot.scan.spi.AbstractScannerImpl;

public class Scanner extends AbstractScannerImpl{

    public Scanner() {
        super( StandardArchiveDescriptor.INSTANCE );
    }


}
