package io.github.pingao777.common.enums;

/**
 * Created by pingao on 2020/1/13.
 */
public enum Phrase {
    JDK_INFO("Jdk Info"),
    MEMORY_INFO("Memory Info"),
    COMMAND_LINE_FLAGS("CommandLine Flags"),
    CMS_ALLOCATION_FAILURE("Allocation Failure"),
    CMS_CONCURRENT_MODE_FAILURE("Concurrent Mode Failure"),
    CMS_INITIAL_MARK("CMS Initial Mark"),
    CMS_CONCURRENT_MARK("CMS Concurrent Mark"),
    CMS_CONCURRENT_PRECLEAN("CMS Concurrent Preclean"),
    CMS_CONCURRENT_ABORTABLE_PRECLEAN("CMS Concurrent Abortable Preclean"),
    CMS_FINAL_REMARK("CMS Final Remark"),
    CMS_CONCURRENT_SWEEP("CMS Concurrent Sweep"),
    CMS_CONCURRENT_RESET("CMS Concurrent Reset"),
    CMS_FULL_GC("Full GC"),
    CMS_GCLOCKER_INITIATED_GC("GCLocker Initiated GC"),
    G1_EVACUATION_PAUSE("G1 Evacuation Pause"),
    G1_HUMONGOUS_PAUSE("G1 Humongous Pause"),
    G1_REMARK("G1 Remark"),
    G1_CLEANUP("G1 Cleanup"),
    G1_CONCURRENT_CLEANUP("Concurrent Cleanup"),
    G1_CONCURRENT_MARK("Concurrent Mark"),
    G1_CONCURRENT_ROOT_REGION_SCAN("Concurrent Root Region Scan"),
    G1_FULL_GC("Full GC")
    ;

    private String name;

    Phrase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
